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
package cn.ieclipse.aorm.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.ieclipse.aorm.Mapping;

/**
 * @author Jamling
 *         
 */
public class MockDatabase extends SQLiteDatabase {
    
    public MockDatabase(String path) {
        super(path);
        dbName = path;
    }
    
    String dbName;
    Map<String, MockCursor> tables = new HashMap<String, MockCursor>();
    
    public void addTable(String table, Class<?> clazz) {
        MockCursor c = tables.get(table);
        Class<?> tableClass = Mapping.getInstance().getTableClass(table);
        if (c == null) {
            List<String> cols = Mapping.getInstance().getColumns(null, clazz);
            c = new MockCursor(cols.toArray(new String[cols.size()]));
            tables.put(table, c);
        }
    }
    
    public long insert(String table, String nullColumnHack,
            ContentValues values) {
        MockCursor c = tables.get(table);
        String[] cols = c.getColumnNames();
        Object[] args = new Object[cols.length];
        int i = 0;
        for (String key : cols) {
            args[i++] = values.get(key);
        }
        Class<?> clazz = Mapping.getInstance().getTableClass(table);
        String pkc = Mapping.getInstance().getPK(clazz);
        int idx = c.getColumnIndex(pkc);
        args[idx] = c.getCount() + 1;
        c.addRow(args);
        return c.getRowSize();
    }
    
    public int update(String table, ContentValues values, String where,
            String[] args) {
        String query = where;
        if (args != null) {
            for (String a : args) {
                query = query.replaceFirst("?", a);
            }
        }
        HashMap<String, Object> vs = new HashMap<String, Object>();
        
        return 0;
    }
    
    public int delete(String table, String where, String[] args) {
        return 0;
    }
    
    @Override
    public Cursor query(boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit) {
            
        MockCursor sub = new MockCursor(columns);
        MockCursor full = tables.get(table);
        // make full rows;
        ArrayList<Object[]> rows = new ArrayList<Object[]>();
        for (int j = 0; j < full.getCount(); j++) {
            ArrayList<Object> list = new ArrayList<Object>();
            for (int i = 0; i < sub.getColumnCount(); i++) {
                String c = sub.getColumnName(i);
                int idx = full.getColumnIndex(c);
                if (idx >= 0) {
                    list.add(full.getString(idx));
                }
                else {
                    list.add(null);
                }
            }
            rows.add(list.toArray(new Object[list.size()]));
        }
        // has where
        if (selection != null) {
            String[] where = selection.split("=");
            String c = where[0].trim();
            String v = where[1].trim();
            if ("?".equals(v)) {
                v = selectionArgs[0];
            }
            
            for (int i = 0; i < rows.size(); i++) {
                Object[] row = rows.get(i);
                int idx = sub.getColumnIndex(c);
                if (idx >= 0) {
                    Object o = row[idx];
                    if (v.equals(o)) {
                        sub.addRow(row);
                    }
                }
            }
        }
        else {
            for (Object[] objects : rows) {
                sub.addRow(objects);
            }
        }
        
        return sub;
    }
    
    public Cursor rawQuery(String sql, String[] args) {
        Matcher m = Pattern.compile("(.*)LIMIT (\\d+) OFFSET (\\d+)")
                .matcher(sql);
        int offset = 0;
        int limit = 0;
        if (m.find()) {
            String[] limitStr = m.replaceAll("$2-$3").split("-");
            limit = Integer.parseInt(limitStr[0]);
            offset = Integer.parseInt(limitStr[1]);
            sql = m.replaceAll("$1");
        }
        String key = "SELECT DISTINCT ";
        int pos = sql.indexOf(key);
        int start = 0;
        if (pos < 0) {
            key = "SELECT ";
            pos = sql.indexOf(key);
        }
        start = pos + key.length();
        
        key = " FROM ";
        int end = sql.indexOf(key);
        String cs = sql.substring(start, end);
        String[] temp = cs.split(",");
        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].trim();
        }
        
        sql = sql.substring(end + key.length());
        start = 0;
        end = sql.indexOf(" ");
        String table = end > 0 ? sql.substring(start, end) : sql;
        
        key = "WHERE ";
        start = sql.indexOf(key);
        MockCursor sub = new MockCursor(temp);
        MockCursor full = tables.get(table);
        
        ArrayList<Object[]> rows = new ArrayList<Object[]>();
        for (int j = 0; j < full.getCount(); j++) {
            full.setRowIndex(j);
            ArrayList<Object> list = new ArrayList<Object>();
            for (int i = 0; i < sub.getColumnCount(); i++) {
                String c = sub.getColumnName(i);
                int idx = full.getColumnIndex(c);
                if (idx >= 0) {
                    list.add(full.getString(idx));
                }
            }
            rows.add(list.toArray(new Object[list.size()]));
        }
        
        if (start > 0) {
            sql = sql.substring(start + key.length());
            String[] where = sql.split("=");
            String c = where[0].trim();
            String v = where[1].trim().split(" ")[0];
            if ("?".equals(v)) {
                v = args[0];
            }
            
            ArrayList<Object[]> dest = new ArrayList<Object[]>();
            int tmp = 0;
            for (int i = 0; i < rows.size(); i++) {
                Object[] row = rows.get(i);
                int idx = sub.getColumnIndex(c);
                if (idx >= 0) {
                    Object o = row[idx];
                    if (v.equals(o)) {
                        if (tmp < offset) {
                            tmp++;
                            continue;
                        }
                        if (limit > 0 && sub.getRowSize() >= limit) {
                            break;
                        }
                        dest.add(row);
                        sub.addRow(row);
                    }
                }
            }
        }
        else {
            for (int i = 0; i < rows.size(); i++) {
                if (i < offset) {
                    continue;
                }
                if (limit > 0 && sub.getRowSize() >= limit) {
                    break;
                }
                sub.addRow(rows.get(i));
            }
        }
        
        return sub;
    }
    
    public void execSQL(String sql) {
    }
    
    public void execSQL(String sql, Object[] args) {
    
    }
    
    private void updateRow(MockCursor c) {
    
    }
}
