/**
 * 
 */
package cn.ieclipse.aorm.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Jamling
 * 
 */
public abstract class BaseActivity extends AppCompatActivity implements
        OnClickListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initIntent(getIntent(), savedInstanceState);
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
    
    protected void initIntent(Intent intent, Bundle savedInstanceState) {
        
    }
    
    protected abstract int getLayout();
}
