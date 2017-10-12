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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;

/**
 * util class to get data from cursor;
 * 
 * @author melord
 *         
 */
public final class CursorUtils {
    private CursorUtils() {
    
    }
    
    public static <T> List<T> getFromCursor(Cursor c, Class<T> objClass,
            String alias) {
        ArrayList<T> list = new ArrayList<T>();
        if (c == null) {
            return list;
        }
        String[] colNames = c.getColumnNames();
        int[] indices = new int[colNames.length];
        Field[] objMethod = new Field[colNames.length];
        // Class<?>[] fieldClass = new Class<?>[colNames.length];
        int[] types = new int[colNames.length];
        try {
            for (int i = 0; i < colNames.length; i++) {
                indices[i] = c.getColumnIndex(colNames[i]);
                Field m = getObjField(colNames[i], objClass, alias);
                if (m != null) {
                    if (!m.isAccessible()) {
                        m.setAccessible(true);
                    }
                    objMethod[i] = m;
                    // fieldClass[i] = m.getType();
                    types[i] = getColumnType(objMethod[i].getType());
                }
            }
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                T obj = objClass.newInstance();
                for (int i = 0; i < colNames.length; i++) {
                    if (objMethod[i] != null) {
                        objMethod[i].set(obj,
                                getColumnValue(c, indices[i], types[i]));
                    }
                }
                list.add(obj);
            }
            c.close();
        } catch (Exception e) {
            throw new ORMException(e);
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> getFromCursor(Cursor c, Criteria criteria) {
        ArrayList<T> list = new ArrayList();
        if (c == null) {
            return list;
        }
        String[] colNames = c.getColumnNames();
        int[] indices = new int[colNames.length];
        Field[] objMethod = new Field[colNames.length];
        // field type class, used in getter invoked.
        // Class<?>[] fieldClass = new Class<?>[colNames.length];
        int[] types = new int[colNames.length];
        
        Class<?> objClass = criteria.getRoot().getClazz();
        String objAlias = criteria.getRoot().getAlias();
        Object obj = null;
        try {
            for (int i = 0; i < colNames.length; i++) {
                indices[i] = c.getColumnIndex(colNames[i]);
                Field m = getObjField(colNames[i], objClass, objAlias);
                if (m != null) {
                    objMethod[i] = m;
                    // fieldClass[i] = objMethod[i].getType();
                    types[i] = getColumnType(objMethod[i].getType());
                }
            }
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                obj = objClass.newInstance();
                for (int i = 0; i < indices.length; i++) {
                    if (objMethod[i] != null) {
                        if (objMethod[i].isAccessible()) {
                            objMethod[i].setAccessible(true);
                        }
                        objMethod[i].set(obj,
                                getColumnValue(c, indices[i], types[i]));
                    }
                }
                list.add((T)obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return list;
    }
    
    public static List<Object[]> getFromCursor(Cursor c,
            Class<?>[] objClassArray, String[] aliasArray) {
            
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        if (c == null) {
            return list;
        }
        String[] colNames = c.getColumnNames();
        int[] indices = new int[colNames.length];
        Field[] objField = new Field[colNames.length];
        // field type class, used in getter invoked.
        // Class<?>[] fieldClass = new Class<?>[colNames.length];
        int[] types = new int[colNames.length];
        int[] objIds = new int[colNames.length];
        try {
            for (int i = 0; i < colNames.length; i++) {
                indices[i] = c.getColumnIndex(colNames[i]);
                for (int j = 0; j < objClassArray.length; j++) {
                    Field f = getObjField(colNames[i], objClassArray[j],
                            aliasArray[j]);
                    // fieldClass[i] = f.getType();
                    types[i] = getColumnType(f.getType());
                    if (f != null) {
                        objField[i] = f;
                        objIds[i] = j;
                        break;
                    }
                }
            }
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                Object[] objArray = new Object[objClassArray.length];
                for (int i = 0; i < indices.length; i++) {
                    Object obj = objArray[objIds[i]];
                    if (obj == null) {
                        obj = objClassArray[objIds[i]].newInstance();
                        objArray[objIds[i]] = obj;
                    }
                    if (objField[i] != null) {
                        objField[i].setAccessible(true);
                        objField[i].set(obj,
                                getColumnValue(c, indices[i], types[i]));
                    }
                }
                list.add(objArray);
            }
            c.close();
        } catch (Exception e) {
            throw new ORMException(e);
        }
        return list;
    }
    
    public static List<Object[]> getFromCursor(Cursor c,
            Class<?>[] objClassArray, int[] separatorArray) {
            
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        if (c == null) {
            return list;
        }
        String[] colNames = c.getColumnNames();
        int[] indices = new int[colNames.length];
        Field[] objMethod = new Field[colNames.length];
        // field type class, used in getter invoked.
        // Class<?>[] fieldClass = new Class<?>[colNames.length];
        int[] types = new int[colNames.length];
        int[] objIdxs = new int[colNames.length];
        try {
            for (int i = 0; i < colNames.length; i++) {
                // Jamling: SQLite3 will not contains table alias in projection
                // column
                indices[i] = i;// c.getColumnIndex(colNames[i]);
                for (int k = 0; k < objClassArray.length; k++) {
                    if (i < separatorArray[k]) {
                        objMethod[i] = getObjField(colNames[i],
                                objClassArray[k]);
                        if (objMethod[i] != null) {
                            // fieldClass[i] = objMethod[i].getType();
                            types[i] = getColumnType(objMethod[i].getType());
                            objIdxs[i] = k;
                        }
                        break;
                    }
                }
            }
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                Object[] objArray = new Object[objClassArray.length];
                for (int i = 0; i < indices.length; i++) {
                    Object obj = objArray[objIdxs[i]];
                    if (obj == null) {
                        obj = objClassArray[objIdxs[i]].newInstance();
                        objArray[objIdxs[i]] = obj;
                    }
                    if (objMethod[i] != null) {
                        if (objMethod[i].isAccessible()) {
                            objMethod[i].setAccessible(true);
                        }
                        objMethod[i].set(obj,
                                getColumnValue(c, indices[i], types[i]));
                    }
                }
                list.add(objArray);
            }
            c.close();
        } catch (Exception e) {
            throw new ORMException(e);
        }
        return list;
    }
    
    private static Field getObjField(String column, Class<?> objClass,
            String alias) {
        Field f = null;
        int pos = column.indexOf('.');
        // has alias
        if (pos > 0) {
            String tempAlias = column.substring(0, pos);
            // match
            if (tempAlias.equals(alias)) {
                f = Mapping.getInstance()
                        .getColumnField(column.substring(pos + 1), objClass);
            }
        }
        else {
            f = Mapping.getInstance().getColumnField(column, objClass);
        }
        return f;
    }
    
    private static Field getObjField(String column, Class<?> objClass) {
        Field f = null;
        int pos = column.indexOf('.');
        // has alias
        if (pos > 0) {
            String tempAlias = column.substring(0, pos);
            // not match
            f = Mapping.getInstance().getColumnField(column.substring(pos + 1),
                    objClass);
        }
        else {
            f = Mapping.getInstance().getColumnField(column, objClass);
        }
        return f;
    }
    
    static String capitalize(String str) {
        String ret = str;
        char c0 = str.charAt(0);
        if (c0 >= 'a' && c0 <= 'z') {
            if (str.length() > 1) {
                char c1 = str.charAt(1);
                if (!(c1 >= 'A' && c1 <= 'Z')) {
                    ret = (char) (c0 - 32) + str.substring(1);
                }
            }
        }
        return ret;
    }
    
    private static int getColumnType(Class<?> type) {
        return MappingFactory.getColumnType(type);
    }
    
    private static Object getColumnValue(Cursor c, int index, int type) {
        return MappingFactory.getColumnValue(c, index, type);
    }
    
    private static Object getColumnValue(Cursor c, int index,
            Class<?> paramClass) {
        Object paramValue = null;
        if (paramClass == int.class || paramClass == Integer.class) {
            paramValue = c.getInt(index);
        }
        else if (paramClass == byte[].class || paramClass == Byte[].class) {
            paramValue = c.getBlob(index);
        }
        else if (paramClass == short.class || paramClass == Short.class) {
            paramValue = c.getShort(index);
        }
        else if (paramClass == long.class || paramClass == Long.class) {
            paramValue = c.getLong(index);
        }
        else if (paramClass == float.class || paramClass == Float.class) {
            paramValue = c.getFloat(index);
        }
        else if (paramClass == double.class || paramClass == Double.class) {
            paramValue = c.getDouble(index);
        }
        else if (paramClass == boolean.class || paramClass == Boolean.class) {
            int temp = c.getInt(index);
            paramValue = temp == 0 ? false : true;
        }
        else if (paramClass == String.class) {
            paramValue = c.getString(index);
        }
        return paramValue;
    }
    
    // ///////////////
    
    static class CursorReflect {
        static Class<?> cursorClass;
        static Method moveToFirst;
        static Method isAfterLast;
        static Method moveToNext;
        
        static Method getColumnNames;
        static Method getColumnIndex;
        static Method close;
        
        static HashMap<String, Method> maps = new HashMap<String, Method>();
        
        static {
            try {
                cursorClass = Class.forName("android.database.Cursor");
                moveToFirst = cursorClass.getDeclaredMethod("moveToFirst",
                        (Class<?>[]) null);
                isAfterLast = cursorClass.getDeclaredMethod("isAfterLast",
                        (Class<?>[]) null);
                moveToNext = cursorClass.getDeclaredMethod("moveToNext",
                        (Class<?>[]) null);
                getColumnNames = cursorClass.getDeclaredMethod("getColumnNames",
                        (Class<?>[]) null);
                getColumnIndex = cursorClass.getDeclaredMethod("getColumnIndex",
                        String.class);
                close = cursorClass.getDeclaredMethod("close",
                        (Class<?>[]) null);
                        
                maps.put("byte[]",
                        cursorClass.getDeclaredMethod("getBlob", int.class));
                maps.put("int",
                        cursorClass.getDeclaredMethod("getInt", int.class));
                maps.put("Integer",
                        cursorClass.getDeclaredMethod("getInt", int.class));
                maps.put("float",
                        cursorClass.getDeclaredMethod("getFloat", int.class));
                // maps.put("Float", cursorClass.getDeclaredMethod("getFloat",
                // int.class));
                maps.put("long",
                        cursorClass.getDeclaredMethod("getLong", int.class));
                // maps.put("Long", cursorClass.getDeclaredMethod("getLong",
                // int.class));
                maps.put("double",
                        cursorClass.getDeclaredMethod("getDouble", int.class));
                // maps.put("Double", cursorClass.getDeclaredMethod("getDouble",
                // int.class));
                maps.put("String",
                        cursorClass.getDeclaredMethod("getString", int.class));
                // maps.put("Short", cursorClass.getDeclaredMethod("getShort",
                // int.class));
                maps.put("short",
                        cursorClass.getDeclaredMethod("getShort", int.class));
            } catch (ClassNotFoundException e) {
                throw new ORMException(
                        "Can't load android.database.Cursor, is android.jar in your classpath?");
            } catch (NoSuchMethodException e) {
                throw new ORMException(e.toString());
            }
        }
        
        static Method getMapping(String type) {
            if ("Integer".equals(type)) {
                return maps.get("int");
            }
            else if ("String".equals(type)) {
                return maps.get("String");
            }
            else {
                return maps.get(type.toLowerCase());
            }
        }
    }
    
    public static List<Object[]> getFromCursorReflect(Object cursor,
            Class<?>[] objClassArray, String[] aliasArray) {
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        try {
            String[] colNames = (String[]) CursorReflect.getColumnNames
                    .invoke(cursor, (Object[]) null);
            int[] indcies = new int[colNames.length];
            Method[] cursorMethods = new Method[colNames.length];
            Field[] objMethod = new Field[colNames.length];
            
            int[] objIdxs = new int[colNames.length];
            
            for (int i = 0; i < colNames.length; i++) {
                indcies[i] = (Integer) CursorReflect.getColumnIndex
                        .invoke(cursor, colNames[i]);
                // String prop = null;
                // int pos = colNames[i].indexOf('.');
                for (int j = 0; j < objClassArray.length; j++) {
                    String alias = aliasArray[j];
                    Class<?> objClass = objClassArray[j];
                    Field m = getObjField(colNames[i], objClass, alias);
                    if (m != null) {
                        objMethod[i] = m;
                        cursorMethods[i] = CursorReflect
                                .getMapping(m.getType().getSimpleName());
                        objIdxs[i] = j;
                        break;
                    }
                }
            }
            for (CursorReflect.moveToFirst.invoke(cursor,
                    (Object[]) null); !(Boolean) CursorReflect.isAfterLast
                            .invoke(cursor,
                                    (Object[]) null); CursorReflect.moveToNext
                                            .invoke(cursor, (Object[]) null)) {
                Object[] objArray = new Object[objClassArray.length];
                // for(int i=0;i<objClassArray.length;i++){
                // objArray[i] = objClassArray[i].newInstance();
                // }
                for (int i = 0; i < indcies.length; i++) {
                    Object obj = objArray[objIdxs[i]];
                    if (obj == null) {
                        obj = objClassArray[objIdxs[i]].newInstance();
                        objArray[objIdxs[i]] = obj;
                    }
                    if (objMethod[i] != null) {
                        objMethod[i].setAccessible(true);
                        objMethod[i].set(obj,
                                cursorMethods[i].invoke(cursor, indcies[i]));
                    }
                }
                list.add(objArray);
            }
            CursorReflect.close.invoke(cursor, (Object[]) null);
            
        } catch (Exception e) {
            throw new ORMException(e);
        }
        return list;
    }
    
    public static <T> List<T> getFromCursorReflect(Object cursor,
            Class<T> objClass, String alias) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            String[] colNames = (String[]) CursorReflect.getColumnNames
                    .invoke(cursor, (Object[]) null);
            int[] indices = new int[colNames.length];
            Method[] cursorMethods = new Method[colNames.length];
            Field[] objMethod = new Field[colNames.length];
            
            for (int i = 0; i < colNames.length; i++) {
                indices[i] = (Integer) CursorReflect.getColumnIndex
                        .invoke(cursor, colNames[i]);
                Field m = getObjField(colNames[i], objClass, alias);
                if (m != null) {
                    objMethod[i] = m;
                    cursorMethods[i] = CursorReflect
                            .getMapping(m.getType().getSimpleName());
                }
            }
            
            for (CursorReflect.moveToFirst.invoke(cursor,
                    (Object[]) null); !(Boolean) CursorReflect.isAfterLast
                            .invoke(cursor,
                                    (Object[]) null); CursorReflect.moveToNext
                                            .invoke(cursor, (Object[]) null)) {
                T obj = objClass.newInstance();
                for (int i = 0; i < indices.length; i++) {
                    if (objMethod[i] != null) {
                        if (objMethod[i].isAccessible()) {
                            objMethod[i].setAccessible(true);
                        }
                        objMethod[i].set(obj,
                                cursorMethods[i].invoke(cursor, indices[i]));
                    }
                }
                list.add(obj);
            }
            CursorReflect.close.invoke(cursor, (Object[]) null);
            
        } catch (Exception e) {
            throw new ORMException(e);
        }
        return list;
    }
}
