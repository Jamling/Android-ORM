package cn.ieclipse.aorm.db;

import org.junit.Assert;
import org.junit.Test;

public class ColumnInfoTest {
    
    @Test
    public void testGetDDL() {
        ColumnInfo c = new ColumnInfo();
        c.name = "id";
        c.pk = 1;
        String expected = "id INTEGER PRIMARY KEY";
        Assert.assertEquals(expected, c.getDDL());
    }
    
    @Test
    public void testSimple() {
        ColumnInfo c = new ColumnInfo();
        c.name = "id";
        c.type = "INTEGER";
        String expected = "id INTEGER";
        Assert.assertEquals(expected, c.getDDL());
    }
    
    @Test
    public void testFULL() {
        ColumnInfo c = new ColumnInfo();
        c.name = "id";
        c.type = "INTEGER";
        c.notnull = 1;
        c.pk = 1;
        c.dflt_value = "''";
        String expected = "id INTEGER PRIMARY KEY NOT NULL DEFAULT ''";
        Assert.assertEquals(expected, c.getDDL());
        
        c.dflt_value = "1";
        expected = "id INTEGER PRIMARY KEY NOT NULL DEFAULT '1'";
        Assert.assertEquals(expected, c.getDDL());
    }
    
}
