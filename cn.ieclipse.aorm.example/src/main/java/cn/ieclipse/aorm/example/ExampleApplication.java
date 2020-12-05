/**
 * 
 */
package cn.ieclipse.aorm.example;

import android.app.Application;
import android.util.Log;
import cn.ieclipse.aorm.Aorm;

/**
 * @author Jamling
 * 
 */
public class ExampleApplication extends Application {
    
    public static final String TAG = "AORM example";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        Aorm.enableDebug(true);
    }
}
