package test.mohitmalhotra.engineerai;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import test.mohitmalhotra.engineerai.callbacks.ActionBarCallBack;
import test.mohitmalhotra.engineerai.models.DataItem;

public class MainActivity extends BaseActivity {

    private static int page=1;
    private static final String API_URL = "https://hn.algolia.com/api/v1/search_by_date?tags=story&page=";

    private OkHttpClient client;
    private ArrayList<DataItem> items;
    private DataAdapter adapter;

    @BindView(R.id.list_view)
    ListView listView;

    @BindView(R.id.mainLayout)
    View mainLayout;

    @BindView(R.id.btnBack)
    Button btnBack;

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.errorLayout) View errorLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ActionMode aMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        items = new ArrayList<>();
        adapter = new DataAdapter(this, items);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = view.findViewById(R.id.checkBox);
                cb.toggle();

                adapter.addSelecttionState(position, cb.isChecked());

                if(cb.isChecked()){
                    if(aMode != null) {
                        aMode = startActionMode(new ActionBarCallBack(adapter));
                    }
                } else{
                    if(adapter.getSelectedCount() == 0){
                        if(aMode != null){
                            aMode.finish();
                        }
                    }
                }
            }
        });

        if(isNetworkAvailable()){
            showErrorView();
        } else{
            showMainView();
        }
        
        new LoadDataTask().execute(page);

        btnNext.setOnClickListener(v->{
            new LoadDataTask().execute(++page);
        });

        btnBack.setOnClickListener(v->{
            new LoadDataTask().execute(--page);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadDataTask extends AsyncTask<Integer, Void, Void>{

        private Request request;

        @Override
        protected void onPreExecute() {
            if(client == null){
                client = new OkHttpClient();
            }

            showProgressView();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            request = new Request.Builder()
                    .url(API_URL + params[0])
                    .build();

            client.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(()-> {
                                showErrorView();
                                Toast.makeText(MainActivity.this, "Unable to fetch the data", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                Log.i("RESPONSE", json.toString());

                                JSONArray dataArray = json.getJSONArray("hits");

                                if(dataArray.length() > 0) {

                                    items.clear();;

                                    for(int i=0; i<dataArray.length(); i++){

                                        JSONObject data = dataArray.getJSONObject(i);

                                        String createdAt = data.getString("created_at");
                                        String date = createdAt.substring(0, createdAt.indexOf("T"));
                                        String time = createdAt.substring(createdAt.indexOf("T") + 1, createdAt.lastIndexOf("."));

                                        DataItem item = new DataItem();
                                        item.setTitle(data.getString("title"));
                                        item.setDate(date);
                                        item.setTime(time);

                                        items.add(item);
                                    }

                                    runOnUiThread(()->adapter.notifyDataSetChanged());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(()-> showErrorView() );
                            }


                            runOnUiThread(()->{ showMainView();
                                if(page == 1){
                                    btnBack.setClickable(false);
                                } else{
                                    btnBack.setClickable(true);
                                }
                            } );
                        }
                    });

            return null;
        }
    }

    private void showErrorView(){

        progressBar.setVisibility(View.GONE);

        if(mainLayout.getVisibility() == View.VISIBLE) {
            mainLayout.setVisibility(View.GONE);
        }

        errorLayout.setVisibility(View.VISIBLE);
    }

    private void showMainView(){

        progressBar.setVisibility(View.GONE);

        errorLayout.setVisibility(View.GONE);

        if(mainLayout.getVisibility() == View.GONE) {
            mainLayout.setVisibility(View.VISIBLE);
        }

    }

    private void showProgressView(){

        if(errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
        }

        if(mainLayout.getVisibility() == View.VISIBLE) {
            mainLayout.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }
}
