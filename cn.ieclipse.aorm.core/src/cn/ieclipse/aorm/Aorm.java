/*
 * Copyright 2010-2014 Jamling(li.jamling@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.aorm;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.ColumnWrap;
import cn.ieclipse.aorm.annotation.Table;
import cn.ieclipse.aorm.db.ColumnInfo;
import cn.ieclipse.aorm.db.TableInfo;

/**
 * Aorm Utils class, e.g. settings, table creation
 * 
 * @author Jamling
 *         
 */
public final class Aorm {
    
    private static boolean debug = false;
    private static boolean supportExtend = true;
    private static boolean exactInsertOrUpdate = false;
    private static final String TAG = "AORM";
    private static String columnPrefix = null;
    
    private Aorm() {
        //
    }
    
    /**
     * Enable/Disable debug to print SQL.
     * 
     * @param enable
     *            debug flag, default false.
     */
    public static void enableDebug(boolean enable) {
        debug = enable;
    }
    
    /**
     * @param allow
     *            true to enable, false to disable
     * @deprecated use {@link #setSupportExtend(boolean)} instead
     */
    @Deprecated
    public static void allowExtend(boolean allow) {
        supportExtend = allow;
    }
    
    /**
     * Set whether support model been support extend or not, default is true.
     * <p>
     * If your model is simple and not extend any other model been, suggested to
     * set <code>false</code> to improve performance.
     * </p>
     * 
     * @param support
     *            true to enable, false to disable
     * @since 1.1.1
     */
    public static void setSupportExtend(boolean support) {
        Aorm.supportExtend = support;
    }
    
    /**
     * Return whether support model been support extend
     * 
     * @return true is support
     * @since 1.1.1
     */
    public static boolean isSupportExtend() {
        return supportExtend;
    }
    
    /**
     * @deprecated use {@link #isSupportExtend()} instead
     * @return allow Java been extend
     */
    @Deprecated
    public static boolean allowExtend() {
        return supportExtend;
    }
    
    /**
     * Set use actuarial insertOrUpdate
     * 
     * @param exactInsertOrUpdate
     *            If true, will query the object from database, insert if not
     *            exists or update if exist, otherwise insert when PK is 0 or
     *            update when PK more than 0 (maybe update fail)
     */
    public static void setExactInsertOrUpdate(boolean exactInsertOrUpdate) {
        Aorm.exactInsertOrUpdate = exactInsertOrUpdate;
    }
    
    /**
     * Get exactInsertOrUpdat
     * 
     * @return whether use actuarial insertOrUpdate strategy
     */
    static boolean getExactInsertOrUpdate() {
        return Aorm.exactInsertOrUpdate;
    }
    
    /**
     * If use implicit mapping (empty name in {@link Column}), will use mapping
     * field name width prefix as database column. Default the prefix is null,
     * and the database column name is same as filed name.
     * 
     * <pre>
     * <code>
     * // the column name will be "age"
     * &#64;Column()
     * public int age;
     * </code>
     * </pre>
     * 
     * After you {@link #setColumnPrefix(String)}, for example the prefix is
     * "_", the database column name will be <var>_age</var>.
     * 
     * @param prefix
     *            column prefix
     * @since 1.1.1
     */
    public static void setColumnPrefix(String prefix) {
        Aorm.columnPrefix = prefix;
    }
    
    /**
     * Return the set column prefix
     * 
     * @return column prefix of implicit mapping.
     */
    public static String getColumnPrefix() {
        return columnPrefix;
    }
    
    /**
     * Print log message on Android using {@link Log android.util.Log}
     * 
     * @param msg
     *            logging message.
     */
    public static void logv(String msg) {
        if (debug) {
            android.util.Log.v(TAG, msg);
        }
    }
    
    public static final String LF = System.getProperty("line.separator");
    
    public static String generateDropDDL(Class<?> tableClass) {
        Table t = tableClass.getAnnotation(Table.class);
        if (t == null) {
            throw new ORMException("No mapping to " + tableClass
                    + ", did you forget add @Table to your class?");
        }
        return "DROP TABLE " + t.name() + " IF EXISTS";
    }
    
    public static String generateCreateDDL(Class<?> tableClass) {
        return generateCreateDDL(tableClass, null);
    }
    
    public static String generateCreateDDL(Class<?> tableClass,
            String tableName) {
        if (tableName == null || tableName.trim().length() == 0) {
            Table t = tableClass.getAnnotation(Table.class);
            if (t == null) {
                throw new ORMException("No mapping to " + tableClass
                        + ", did you forget add @Table to your class?");
            }
            tableName = t.name();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(");
        sb.append(LF);
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        for (ColumnWrap cw : list) {
            sb.append(new ColumnMeta(cw).toSQL());
            sb.append(", ");
            sb.append(LF);
        }
        int len = sb.length() - 2;
        len = len - LF.length();
        sb.delete(len, sb.length());
        sb.append(")");
        sb.append(LF);
        return (sb.toString());
    }
    
    private static String generateColumnDDL(Class<?> tableClass,
            String delimiter) {
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        String[] columns = new String[list.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new ColumnMeta(list.get(i)).toSQL();
        }
        return Aorm.join(delimiter, columns);
    }
    
    /**
     * Get table column projection
     * 
     * @param tableClass
     *            mapping class
     * @param delimiter
     *            delimiter default is ","
     * @return projection (joined column with delimiter)
     * @since 1.1.4
     */
    public static String getProjections(Class<?> tableClass, String delimiter) {
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        String[] columns = new String[list.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = list.get(i).getColumnName();
        }
        if (Aorm.empty(delimiter)) {
            delimiter = ",";
        }
        return Aorm.join(delimiter, columns);
    }
    
    public static void createTable(SQLiteDatabase db, Class<?> tableClass) {
        String sql = generateCreateDDL(tableClass);
        db.execSQL(sql);
    }
    
    public static void dropTable(SQLiteDatabase db, Class<?> tableClass) {
        String sql = generateDropDDL(tableClass);
        db.execSQL(sql);
    }
    
    /**
     * Update table structure
     * 
     * @param db
     *            the sqlite database
     * @param tableClass
     *            mapping class
     * @since 1.1.4
     */
    public static void updateTable(SQLiteDatabase db, Class<?> tableClass) {
        Table t = tableClass.getAnnotation(Table.class);
        if (t == null) {
            throw new ORMException("No mapping to " + tableClass
                    + ", did you forget add @Table to your class?");
        }
        // old table column
        List<ColumnInfo> old = Aorm.getColumnInfo(db, t.name());
        // new table column
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        // added columns
        
        for (int i = 0; i < old.size(); i++) {
            ColumnInfo c = old.get(i);
            
            boolean found = false;
            for (int j = 0; j < list.size(); j++) {
                ColumnWrap wrap = list.get(j);
                if (c.name.equals(wrap.getColumnName())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                
            }
        }
    }
    
    /**
     * Update table structure
     * 
     * @param db
     *            the sqlite database
     * @param tableClass
     *            mapping class
     * @since 1.1.4
     */
    private static void updateTableFully(SQLiteDatabase db,
            Class<?> tableClass) {
        Table t = tableClass.getAnnotation(Table.class);
        if (t == null) {
            throw new ORMException("No mapping to " + tableClass
                    + ", did you forget add @Table to your class?");
        }
        // old table column
        List<ColumnInfo> old = Aorm.getColumnInfo(db, t.name());
        // new table column
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        
        List<String> clist1 = new ArrayList<String>();
        List<String> ddllist1 = new ArrayList<String>();
        for (int i = 0; i < old.size(); i++) {
            ColumnInfo c = old.get(i);
            
            boolean found = false;
            for (int j = 0; j < list.size(); j++) {
                ColumnWrap wrap = list.get(j);
                if (c.name.equals(wrap.getColumnName())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                clist1.add(c.name);
                ddllist1.add(c.getDDL());
            }
        }
        String oldPrj = Aorm.join(",", clist1.toArray());
        String oldDdl = Aorm.join(",", ddllist1.toArray());
        
        List<String> clist2 = new ArrayList<String>();
        String[] newColumns = new String[list.size()];
        for (int i = 0; i < newColumns.length; i++) {
            ColumnWrap wrap = list.get(i);
            newColumns[i] = new ColumnMeta(wrap).toSQL();
            boolean found = false;
            for (int j = 0; j < clist1.size(); j++) {
                if (clist1.get(j).equalsIgnoreCase(wrap.getColumnName())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                clist2.add(wrap.getColumnName());
            }
        }
        // new insert projection = new table column - added column
        String newPrj = Aorm.join(",", clist2.toArray());
        String newDdl = Aorm.join(",", newColumns);
        
        String tempName = Aorm.getTempTableName(db, t.name());
        String createTempTable = String.format("CREATE TEMPORARY TABLE %s(%s);",
                tempName, oldDdl);
        String insertTempTable = String.format(
                "INSERT INTO %s SELECT %s FROM %s;", tempName, oldPrj,
                t.name());
        String dropTable = String.format("DROP TABLE  %s", t.name());
        
        String createTable = String.format("CREATE TABLE %s (%s);", t.name(),
                newDdl);
        String insertTable = String.format(
                "INSERT INTO %s(%s) SELECT %s FROM %s;", t.name(), newPrj,
                newPrj, tempName);
        String dropTempTable = String.format("DROP TABLE %s;", tempName);
        db.beginTransaction();
        db.execSQL(createTempTable);
        db.execSQL(insertTempTable);
        db.execSQL(dropTable);
        db.endTransaction();
        db.beginTransaction();
        db.execSQL(createTable);
        db.execSQL(insertTable);
        db.execSQL(dropTempTable);
        db.endTransaction();
    }
    
    /**
     * Get the sqlite database table columns info
     * 
     * @param db
     *            the sqlite database
     * @param tableName
     *            table name
     * @return {@link ColumnInfo} collection
     * @since 1.1.4
     */
    public static List<ColumnInfo> getColumnInfo(SQLiteDatabase db,
            String tableName) {
        String sql = String.format("PRAGMA TABLE_INFO(%s);", tableName);
        Cursor c = db.rawQuery(sql, null);
        List<ColumnInfo> columns = CursorUtils.getFromCursor(c,
                ColumnInfo.class, null);
        return columns;
    }
    
    private static String getTempTableName(SQLiteDatabase db, String table) {
        List<TableInfo> tables = Aorm.getTableInfos(db);
        int index = 1;
        String base = table + "_temp_";
        while (true) {
            boolean found = false;
            for (TableInfo t : tables) {
                if (t.same(base + index)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                index++;
            }
            else {
                break;
            }
        }
        return base + index;
    }
    
    /**
     * Get the sqlite database table info
     * 
     * @param db
     *            the sqlite database
     * @return {@link TableInfo} collection
     * @since 1.1.4
     */
    public static List<TableInfo> getTableInfos(SQLiteDatabase db) {
        String sql = "select name from sqlite_master where type='table' order by name;";
        Cursor c = db.rawQuery(sql, null);
        List<TableInfo> tables = CursorUtils.getFromCursor(c, TableInfo.class,
                null);
        return tables;
    }
    
    private static boolean empty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }
    
    /**
     * @param delimiter
     *            the delimiter
     * @param elements
     *            the source string
     * @return joined string
     * @since 1.1.4
     */
    public static String join(CharSequence delimiter, Object[] elements) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object element : elements) {
            if (firstTime) {
                firstTime = false;
            }
            else {
                sb.append(delimiter);
            }
            sb.append(element);
        }
        return sb.toString();
    }
    
}
