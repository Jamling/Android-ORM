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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import cn.ieclipse.aorm.annotation.ColumnWrap;
import cn.ieclipse.aorm.annotation.TableWrap;

/**
 * @author Jamling
 */
public class Mapping {
    private static Mapping instance = null;
    
    public static Mapping getInstance() {
        if (instance == null) {
            synchronized (Mapping.class) {
                instance = new Mapping();
            }
        }
        return instance;
    }
    
    private HashMap<Class<?>, TableWrap> tables = new HashMap<Class<?>, TableWrap>();
    
    private TableWrap getTableWrap(Class<?> clazz) {
        TableWrap wrap = tables.get(clazz);
        if (wrap == null) {
            wrap = new TableWrap(clazz);
            tables.put(clazz, wrap);
        }
        return wrap;
    }
    
    public String getTableName(Class<?> clazz) {
        return getTableWrap(clazz).getTableName();
    }
    
    /**
     * <p>
     * Get mapping table class by specified table name.
     * </p>
     * <strong>Warning:</strong> If class not in mapping table, will return
     * null.
     * 
     * @param table
     *            table name in database
     * @return mapped class.
     */
    public Class<?> getTableClass(String table) {
        for (TableWrap wrap : tables.values()) {
            if (wrap.getTableName().equals(table)) {
                return wrap.getTableClass();
            }
        }
        return null;
    }
    
    public List<String> getColumns(String alias, Class<?> clazz) {
        return getTableWrap(clazz).getColumnProjection(alias);
    }
    
    public String getColumnName(String property, Class<?> clazz) {
        return getTableWrap(clazz).getColumn(property);
    }
    
    public String getPropertyName(String column, Class<?> clazz) {
        return getTableWrap(clazz).getProperty(column);
    }
    
    public String getPK(Class<?> clazz) {
        return getTableWrap(clazz).getPK();
    }
    
    public String getPKProperty(Class<?> clazz) {
        return getTableWrap(clazz).getPKProperty();
    }
    
    /* package */ColumnWrap getColumn(String property, Class<?> clazz) {
        return getTableWrap(clazz).getColumnWrap(property);
    }
    
    /* package */List<ColumnWrap> getColumns(Class<?> clazz) {
        return getTableWrap(clazz).getColumnWraps();
    }
    
    @Deprecated
    /* package */Method getGetterByColumn(String column, Class<?> clazz) {
        return getTableWrap(clazz).getGetterByColumn(column);
    }
    
    @Deprecated
    /* package */Method getSetterByColumn(String column, Class<?> clazz) {
        return getTableWrap(clazz).getSetterByColumn(column);
    }
    
    /* package */Field getColumnField(String column, Class<?> clazz) {
        return getTableWrap(clazz).getColumnField(column);
    }
}
