/**
 * 
 */
package cn.ieclipse.aorm.example;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author Jamling
 * 
 */
public abstract class ListActivity extends BaseActivity implements
        OnItemClickListener {
    
    private ListView lvList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        lvList = (ListView) findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(this);
        lvList.setAdapter(initAdapter());
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        onUpdateAdapter();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            add();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected abstract void add();
    
    protected abstract ListAdapter initAdapter();
    
    protected abstract ListAdapter onUpdateAdapter();
}
