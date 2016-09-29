/*
 * Copyright 2014-2015 ieclipse.cn.
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

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.ColumnWrap;

/**
 * Column annotation meta-data
 * 
 * @author Jamling
 */
public class ColumnMeta {
    String name;
    String type;
    
    boolean haveDefaultValue;
    String defaultValue;
    
    boolean haveNotNull;
    boolean notNull;
    
    boolean haveId;
    boolean id;
    
    public ColumnMeta() {
    
    }
    
    public ColumnMeta(ColumnWrap wrap) {
        Column c = wrap.getColumn();
        id = c.id();
        notNull = c.notNull();
        defaultValue = c.defaultValue();
        type = getDbColType(wrap);
        name = wrap.getColumnName();
    }
    
    public String toSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(' ');
        
        if (id) {
            sb.append("INTEGER");
            sb.append(' ');
            sb.append("PRIMARY KEY AUTOINCREMENT");
        }
        else {
            sb.append(type);
        }
        sb.append(notNull ? " NOT NULL " : "");
        // if (defaultValue != null) {
        // sb.append("Default '");
        // sb.append(defaultValue == null ? "" : defaultValue);
        // sb.append("'");
        // }
        return sb.toString();
    }
    
    String getDbColType(ColumnWrap wrap) {
        String type = wrap.getColumn().type();
        if (type == null || type.isEmpty()) {
            Class<?> clz = wrap.getFieldType();
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
            else {
                type = "TEXT";
            }
        }
        
        if (type.startsWith("java.lang.")) {
            type = type.substring("java.lang.".length());
        }
        return type;
    }
}
