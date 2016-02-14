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
    
    public ColumnMeta(Column c) {
        id = c.id();
        notNull = c.notNull();
        defaultValue = c.defaultValue();
        type = c.type();
        name = c.name();
        if (type.startsWith("java.lang.")) {
            type = type.substring("java.lang.".length());
        }
    }
    
    public String toSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(' ');
        
        if (id) {
            sb.append("Integer");
            sb.append(' ');
            sb.append("Primary key autoincrement");
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
}
