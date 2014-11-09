package cn.ieclipse.aorm.test;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jamling
 * 
 */
public class MockOpenHelper extends SQLiteOpenHelper {
    
    private MockDatabase db;
    
    public MockOpenHelper(String name) {
        this(name, 0);
    }
    
    public MockOpenHelper(String name, int version) {
        super(name, version);
        this.db = new MockDatabase(name);
    }
    
    @Override
    public SQLiteDatabase getReadableDatabase() {
        return db;
    }
    
    @Override
    public SQLiteDatabase getWritableDatabase() {
        return db;
    }
}
