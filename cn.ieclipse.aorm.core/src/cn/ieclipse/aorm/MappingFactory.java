/*
 * Copyright 2014-2017 ieclipse.cn.
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

import android.database.Cursor;

/**
 * The mapping factory
 * 
 * @author Jamling
 * @date 2017/07/27
 *       
 * @since 1.1.4
 */
public abstract class MappingFactory {
    static final int COL_UNKNOWN = -1;
    static final int COL_INT = 0;
    static final int COL_LONG = 1;
    static final int COL_SHORT = 2;
    static final int COL_FLOAT = 4;
    static final int COL_DOUBLE = 5;
    static final int COL_BLOB = 17;
    static final int COL_STRING = 16;
    static final int COL_BOOLEAN = 18;
    private static MappingFactory instance;
    
    /**
     * Convert Java bean property field type to database column type.
     * 
     * @param fieldType
     *            property field type
     * @return column data type of sqlite
     */
    public abstract String propertyToColumnType(Class<?> fieldType);
    
    /**
     * Get the instance;
     * 
     * @return {@link MappingFactory}
     */
    public static MappingFactory getInstance() {
        if (instance == null) {
            synchronized (MappingFactory.class) {
                instance = new DefaultMappingFactory();
            }
        }
        return instance;
    }
    
    public static class DefaultMappingFactory extends MappingFactory {
        @Override
        public String propertyToColumnType(Class<?> fieldType) {
            String type = fieldType.getSimpleName();
            Class<?> clz = fieldType;
            if (int.class == clz || java.lang.Integer.class.equals(clz)
                    || long.class == clz || java.lang.Long.class.equals(clz)
                    || short.class == clz || java.lang.Short.class == clz) {
                type = "INTEGER";
            }
            else if (boolean.class == clz || java.lang.Boolean.class == clz) {
                type = "INTEGER";
            }
            
            else if (float.class == clz || java.lang.Float.class == clz
                    || double.class == clz || java.lang.Double.class == clz) {
                type = "NUMERIC";
            }
            else if (byte[].class == clz) {
                type = "BLOB";
            }
            else if (String.class == clz) {
                type = "TEXT";
            }
            return type;
        }
    }
    
    static int getColumnType(Class<?> paramClass) {
        if (paramClass == int.class || paramClass == Integer.class) {
            return MappingFactory.COL_INT;
        }
        else if (paramClass == short.class || paramClass == Short.class) {
            return MappingFactory.COL_SHORT;
        }
        else if (paramClass == long.class || paramClass == Long.class) {
            return MappingFactory.COL_LONG;
        }
        else if (paramClass == String.class) {
            return MappingFactory.COL_STRING;
        }
        else if (paramClass == float.class || paramClass == Float.class) {
            return MappingFactory.COL_FLOAT;
        }
        else if (paramClass == double.class || paramClass == Double.class) {
            return MappingFactory.COL_DOUBLE;
        }
        else if (paramClass == boolean.class || paramClass == Boolean.class) {
            return MappingFactory.COL_BOOLEAN;
        }
        else if (paramClass == byte[].class || paramClass == Byte[].class) {
            return MappingFactory.COL_BLOB;
        }
        return MappingFactory.COL_UNKNOWN;
    }
    
    static Object getColumnValue(Cursor c, int index, int colType) {
        Object paramValue = null;
        if (MappingFactory.COL_INT == colType) {
            paramValue = c.getInt(index);
        }
        else if (MappingFactory.COL_LONG == colType) {
            paramValue = c.getLong(index);
        }
        else if (MappingFactory.COL_SHORT == colType) {
            paramValue = c.getShort(index);
        }
        else if (MappingFactory.COL_STRING == colType) {
            paramValue = c.getString(index);
        }
        else if (MappingFactory.COL_FLOAT == colType) {
            paramValue = c.getFloat(index);
        }
        else if (MappingFactory.COL_DOUBLE == colType) {
            paramValue = c.getDouble(index);
        }
        else if (MappingFactory.COL_BOOLEAN == colType) {
            int temp = c.getInt(index);
            paramValue = temp == 0 ? false : true;
        }
        else if (MappingFactory.COL_BLOB == colType) {
            paramValue = c.getBlob(index);
        }
        return paramValue;
    }
}
