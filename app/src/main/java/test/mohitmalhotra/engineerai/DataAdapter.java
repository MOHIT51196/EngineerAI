package test.mohitmalhotra.engineerai;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.mohitmalhotra.engineerai.models.DataItem;

/**
 * Created by MOHIT MALHOTRA on 21-08-2018.
 */

public class DataAdapter extends BaseAdapter {

    private List<DataItem> items;
    private Context context;
    private List<Boolean> selectedItems;

    public DataAdapter (Context context,List<DataItem> items) {
        this.items = items;
        this.context = context;
        this.selectedItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);

        DataItem item = items.get(position);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvTime = view.findViewById(R.id.tvTime);
        CheckBox cb = view.findViewById(R.id.checkBox);

        tvTitle.setText(item.getTitle());
        tvDate.setText(item.getDate());
        tvTime.setText(item.getTime());
        cb.setSelected(selectedItems.get(position));

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        int oldSize = selectedItems.size();

        for(int i=0; i<items.size() - oldSize; i++){
            selectedItems.add(false);
        }
    }

    public int getSelectedCount(){
        int count = 0;
        for(boolean state : selectedItems){
            if(state) count++;
        }
        return count;
    }

    public void addSelecttionState(int position, boolean state){
        selectedItems.set(position, state);
    }


}
