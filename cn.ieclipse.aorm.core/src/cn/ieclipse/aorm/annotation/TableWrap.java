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
package cn.ieclipse.aorm.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.aorm.Aorm;
import cn.ieclipse.aorm.ORMException;

/**
 * @author Jamling
 * 
 */
public class TableWrap {
    private Class<?> clazz;
    private Table table;
    private String pkColName;
    private String pkProName;
    private ArrayList<ColumnWrap> columns = new ArrayList<ColumnWrap>();
    
    public TableWrap(Class<? extends Object> clazz) {
        Table t = clazz.getAnnotation(Table.class);
        if (t != null) {
            table = t;
            this.clazz = clazz;
            List<Field> fields = getClassField(this.clazz);
            if (fields != null) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        Column c = field.getAnnotation(Column.class);
                        ColumnWrap wrap = new ColumnWrap(c, field);
                        columns.add(wrap);
                        if (c.id()) {
                            pkColName = c.name();
                            pkProName = field.getName();
                        }
                    }
                }
            }
        }
        else {
            throw new ORMException(
                    "No mapping for "
                            + clazz.getName()
                            + ", did you written Table annotation before class declaration?");
        }
    }
    
    private List<Field> getClassField(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        getParentClassField(clazz, list);
        return list;
    }
    
    private void getParentClassField(Class<?> clazz, List<Field> list) {
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    list.add(field);
                }
            }
            if (Aorm.allowExtend()) {
                getParentClassField(clazz.getSuperclass(), list);
            }
        }
    }
    
    public String getColumn(String property) {
        if (property == null) {
            throw new NullPointerException();
        }
        String colName = null;
        for (ColumnWrap col : columns) {
            if (col.getPropertyName().equals(property)) {
                colName = col.getColumnName();
                break;
            }
        }
        // when no assign alias to criteria, we should try to find
        // column in criteria tree. so we can't throw ORMException
        // you should check null return value manually.
        return colName;
    }
    
    public Method getGetterByColumn(String column) {
        Method getter = null;
        for (ColumnWrap col : columns) {
            if (col.getColumnName().equals(column)) {
                try {
                    getter = clazz
                            .getMethod(col.getGetter(), (Class<?>[]) null);
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
        return getter;
    }
    
    public Method getSetterByColumn(String column) {
        Method getter = null;
        for (ColumnWrap col : columns) {
            if (col.getColumnName().equals(column)) {
                try {
                    getter = clazz.getMethod(col.getSetter(),
                            col.getFieldType());
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
        return getter;
    }
    
    public String getPK() {
        // String colName = null;
        // for (ColumnWrap col : columns) {
        // if (col.getColumn().id()) {
        // colName = col.getColumnName();
        // break;
        // }
        // }
        //
        // return colName;
        return pkColName;
    }
    
    public String getPKProperty() {
        return pkProName;
    }
    
    public ColumnWrap getColumnWrap(String property) {
        if (property == null) {
            throw new NullPointerException();
        }
        ColumnWrap colName = null;
        for (ColumnWrap col : columns) {
            if (col.getPropertyName().equals(property)) {
                colName = col;
                break;
            }
        }
        return colName;
    }
    
    public List<ColumnWrap> getColumnWraps() {
        return columns;
    }
    
    public String getProperty(String column) {
        String propName = null;
        for (ColumnWrap col : columns) {
            if (col.getColumnName().equals(column)) {
                propName = col.getPropertyName();
                break;
            }
        }
        return propName;
    }
    
    public ArrayList<String> getColumnProjection(String alias) {
        ArrayList<String> cols = new ArrayList<String>(columns.size());
        boolean hasAlias = alias != null && !"".equals(alias.trim());
        
        if (hasAlias) {
            String preffix = alias + ".";
            for (ColumnWrap col : columns) {
                cols.add(preffix + col.getColumnName());
            }
        }
        else {
            for (ColumnWrap col : columns) {
                cols.add(col.getColumnName());
            }
        }
        return cols;
    }
    
    public ArrayList<String> getPropertyProjection(String alias) {
        ArrayList<String> cols = new ArrayList<String>(columns.size());
        boolean hasAlias = alias != null && !"".equals(alias.trim());
        
        if (hasAlias) {
            String preffix = alias + ".";
            for (ColumnWrap col : columns) {
                cols.add(preffix + col.getPropertyName());
            }
        }
        else {
            for (ColumnWrap col : columns) {
                cols.add(col.getPropertyName());
            }
        }
        return cols;
    }
    
    public String getTableName() {
        return table.name();
    }
    
    public Class<?> getTableClass() {
        return clazz;
    }
    
    @Override
    public String toString() {
        return "Table(" + table.name() + ")";
    }
}
