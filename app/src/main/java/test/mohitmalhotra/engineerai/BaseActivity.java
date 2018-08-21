package test.mohitmalhotra.engineerai;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by MOHIT MALHOTRA on 21-08-2018.
 */

public class BaseActivity extends AppCompatActivity {

    public boolean isNetworkAvailable(){

        ConnectivityManager connectManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }
}
