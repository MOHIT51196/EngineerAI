package test.mohitmalhotra.engineerai.callbacks;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import test.mohitmalhotra.engineerai.DataAdapter;

public class ActionBarCallBack implements ActionMode.Callback {

    private DataAdapter adapter;

    public ActionBarCallBack(DataAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            return false;
        }
  
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
//            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return false;
        }
  
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub
  
        }
  
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
  
            mode.setTitle("Selected " + adapter.getSelectedCount() + " items");
            return false;
        }
}