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

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.ColumnWrap;
import cn.ieclipse.aorm.annotation.Table;

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
        Table t = tableClass.getAnnotation(Table.class);
        if (t == null) {
            throw new ORMException("No mapping to " + tableClass
                    + ", did you forget add @Table to your class?");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(t.name());
        sb.append("(");
        sb.append(LF);
        List<ColumnWrap> list = Mapping.getInstance().getColumns(tableClass);
        for (ColumnWrap cw : list) {
            sb.append(new ColumnMeta(cw).toSQL()).toString();
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
    
    public static void createTable(SQLiteDatabase db, Class<?> tableClass) {
        String sql = generateCreateDDL(tableClass);
        db.execSQL(sql);
    }
    
    public static void dropTable(SQLiteDatabase db, Class<?> tableClass) {
        String sql = generateDropDDL(tableClass);
        db.execSQL(sql);
    }
}
