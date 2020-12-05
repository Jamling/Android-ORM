/**
 * 
 */
package cn.ieclipse.aorm.example;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import cn.ieclipse.aorm.Session;

/**
 * @author Jamling
 * 
 */
public abstract class DetailActivity extends BaseActivity {
    
    protected long id;
    protected EditText etIndex;
    protected Session session;
    
    @Override
    protected void initIntent(Intent intent, Bundle savedInstanceState) {
        id = getIntent().getLongExtra(Intent.EXTRA_UID, 0);
        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(Intent.EXTRA_UID);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // menu.findItem(R.id.menu_edit).setVisible(true);
        // menu.findItem(R.id.menu_delete).setVisible(true);
        // menu.findItem(R.id.menu_save).setVisible(true);
        if (id == 0) {
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_delete).setVisible(false);
        }
        else {
            menu.findItem(R.id.menu_save).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            if (save()) {
                updateOptionMenu();
                etIndex.setText(String.valueOf(id));
                Toast.makeText(this, "Save successfully!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (item.getItemId() == R.id.menu_edit) {
            if (edit()) {
                updateOptionMenu();
                Toast.makeText(this, "Update successfully!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (item.getItemId() == R.id.menu_delete) {
            if (delete()) {
                updateOptionMenu();
                id = 0;
                etIndex.setText(String.valueOf(id));
                Toast.makeText(this, "Delete successfully!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(Intent.EXTRA_UID, id);
        super.onSaveInstanceState(outState);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void updateOptionMenu() {
        invalidateOptionsMenu();
    }
    
    protected abstract int getLayout();
    
    protected abstract boolean delete();
    
    protected abstract boolean edit();
    
    protected abstract boolean save();
}
