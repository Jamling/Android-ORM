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
import cn.ieclipse.aorm.MappingFactory.DefaultMappingFactory;
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
    private static MappingFactory mappingFactory = null;
    
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
     * Set {@link MappingFactory}
     * 
     * @param mappingFactory
     *            {@link MappingFactory} instance
     * @since 1.1.4
     */
    public static void setMappingFactory(MappingFactory mappingFactory) {
        Aorm.mappingFactory = mappingFactory;
    }
    
    /**
     * Get {@link MappingFactory}, if no set, the {@link DefaultMappingFactory}
     * instance will be returned.
     * 
     * @return current {@link MappingFactory}
     * @since 1.1.4
     */
    public static MappingFactory getMappingFactory() {
        if (mappingFactory == null) {
            mappingFactory = MappingFactory.getInstance();
        }
        return mappingFactory;
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
    
    /**
     * 
     * see {@link android.util.Log#i(String, String)}
     * 
     * @param msg
     *            message
     * @since 1.1.4
     */
    public static void logi(String msg) {
        android.util.Log.i(TAG, msg);
    }
    
    public static final String LF = System.getProperty("line.separator");
    
    public static String generateDropDDL(Class<?> tableClass) {
        Table t = Mapping.getInstance().getTable(tableClass);
        return "DROP TABLE " + t.name() + " IF EXISTS";
    }
    
    public static String generateCreateDDL(Class<?> tableClass) {
        return generateCreateDDL(tableClass, null);
    }
    
    public static String generateCreateDDL(Class<?> tableClass,
            String tableName) {
        if (tableName == null || tableName.trim().length() == 0) {
            Table t = Mapping.getInstance().getTable(tableClass);
            tableName = t.name();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(");
        sb.append(LF);
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        for (ColumnWrap cw : list) {
            sb.append(ColumnInfo.from(cw).getDDL());
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
    
    private static String generateColumnDDL(List<ColumnInfo> list,
            String delimiter) {
        Object[] columns = new String[list.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = list.get(i).getDDL();
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
    private static String getProjections(Class<?> tableClass,
            String delimiter) {
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        Object[] columns = new Object[list.size()];
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
        Table t = Mapping.getInstance().getTable(tableClass);
        // old table column
        List<ColumnInfo> older = Aorm.getColumnInfo(db, t.name());
        // new table column
        List<ColumnInfo> newer = ColumnInfo
                .from(Mapping.getInstance().getColumns(tableClass));
        // added columns
        List<ColumnInfo> added = new ArrayList<ColumnInfo>();
        List<ColumnInfo> changed = new ArrayList<ColumnInfo>();
        List<String> projections = new ArrayList<String>();
        
        for (int i = 0; i < older.size(); i++) {
            ColumnInfo c = older.get(i);
            ColumnInfo n = Aorm.find(c, newer);
            if (n == null || !c.equals(n)) {
                changed.add(c);
            }
            if (n != null) {
                projections.add(c.name);
            }
        }
        
        for (int i = 0; i < newer.size(); i++) {
            ColumnInfo c = newer.get(i);
            ColumnInfo n = Aorm.find(c, older);
            if (n == null) {
                added.add(c);
            }
        }
        
        if (changed.isEmpty()) {
            if (!added.isEmpty()) {
                Aorm.logi("Add column to " + t.name());
                for (ColumnInfo info : added) {
                    String sql = String.format("ALTER TABLE %s ADD COLUMN %s",
                            t.name(), info.getDDL());
                    Aorm.logi(sql);
                    db.execSQL(sql);
                }
            }
        }
        else {
            Aorm.logi("Change schema of " + t.name());
            Aorm.changeSchema(db, t.name(), older, newer, projections);
        }
    }
    
    private static ColumnInfo find(ColumnInfo src, List<ColumnInfo> dest) {
        for (int i = 0; i < dest.size(); i++) {
            ColumnInfo ret = dest.get(i);
            if (src.name.equals(ret.name)) {
                return ret;
            }
        }
        return null;
    }
    
    /**
     * see <a href=
     * "linkplain http://www.sqlite.org/lang_altertable.html" >http://www.sqlite
     * .org/lang_altertable.html</a>
     * 
     * @param db
     * @param tableName
     * @param older
     * @param newer
     */
    private static void changeSchema(SQLiteDatabase db, String tableName,
            List<ColumnInfo> older, List<ColumnInfo> newer,
            List<String> projections) {
        // step 1 TODO
        // db.execSQL("PRAGMA foreign_keys=OFF");
        // step2 don't use transaction
        // db.beginTransaction();
        // step3
        List<TableInfo> tableInfos = Aorm.getTableInfos(db, tableName, null);
        
        String ddl1 = Aorm.generateColumnDDL(older, ",");
        String prj1 = Aorm.join(",", projections.toArray());
        String ddl2 = Aorm.generateColumnDDL(newer, ",");
        // step 4-7
        String tempName = Aorm.getTempTableName(db, tableName);
        String createTempTable = String.format("CREATE TABLE %s(%s)", tempName,
                ddl2);
        String insertTempTable = String.format(
                "INSERT INTO %s(%s) SELECT %s FROM %s", tempName, prj1, prj1,
                tableName);
        String dropTable = String.format("DROP TABLE  %s", tableName);
        
        String renameTable = String.format("ALTER TABLE %s RENAME TO %s",
                tempName, tableName);
        Aorm.logi(createTempTable);
        db.execSQL(createTempTable);
        Aorm.logi(insertTempTable);
        db.execSQL(insertTempTable);
        Aorm.logi(dropTable);
        db.execSQL(dropTable);
        Aorm.logi(renameTable);
        db.execSQL(renameTable);
        
        // step 8
        for (TableInfo info : tableInfos) {
            if (info.isIndex() || info.isTrigger()) {
                db.execSQL(info.sql);
            }
        }
        // step 9 TODO alter view from table
        // step 10 TODO PRAGMA foreign_key_check
        // step 11
        // db.endTransaction();
        // step 12 TODO db.execSQL("PRAGMA foreign_keys=ON");
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
        List<TableInfo> tables = Aorm.getTableInfos(db, null, "table");
        int index = 1;
        String base = table + "_temp_";
        while (true) {
            boolean found = false;
            for (TableInfo t : tables) {
                if (t.isTable() && t.name.equalsIgnoreCase((base + index))) {
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
     * @param tableName
     *            table name
     * @param type
     *            type ["index"|"table"|"trigger"]
     * @return {@link TableInfo} collection
     * @since 1.1.4
     */
    public static List<TableInfo> getTableInfos(SQLiteDatabase db,
            String tableName, String type) {
        List<String> where = new ArrayList<String>(2);
        List<String> args = new ArrayList<String>(2);
        
        String sql = "select * from sqlite_master";
        if (!Aorm.empty(tableName)) {
            where.add("tbl_name = '" + tableName + "'");
            args.add(tableName);
        }
        if (!Aorm.empty(type)) {
            where.add("type = '" + type + "'");
            args.add(type);
        }
        if (!where.isEmpty()) {
            sql += " where " + Aorm.join(" and ", where.toArray());
        }
        Aorm.logv(sql);
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
    public static String join(CharSequence delimiter, Object... elements) {
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
