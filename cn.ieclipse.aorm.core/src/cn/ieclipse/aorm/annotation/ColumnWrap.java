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

import cn.ieclipse.aorm.Aorm;

/**
 * @author Jamling
 * 
 */
public class ColumnWrap {
    private Column column;
    private String columnName;
    private Field field;
    private Class<?> fieldType;
    private String getter;
    private String setter;
    
    public ColumnWrap(Column column, Field field) {
        this.column = column;
        this.field = field;
        if (!this.field.isAccessible()) {
            this.field.setAccessible(true);
        }
        this.fieldType = (Class<?>) field.getType();
        
//        String fieldName = field.getName();
//        if (boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
//            if (fieldName.startsWith("is") && fieldName.length() > 2) {
//                fieldName = fieldName.substring(2);
//            }
//            getter = "is" + capitalize(fieldName);
//        }
//        else {
//            getter = "get" + capitalize(fieldName);
//        }
//        
//        setter = "set" + capitalize(fieldName);
        
        String name = column.name();
        if (name == null || name.isEmpty()) {
            name = getPropertyName();
            String prefix = Aorm.getColumnPrefix();
            if (prefix != null && !prefix.isEmpty()) {
                name = prefix + name;
            }
        }
        this.columnName = name;
    }
    
    public String getPropertyName() {
        return field.getName();
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public Field getField() {
        return field;
    }
    
    public Column getColumn() {
        return column;
    }
    
    public String getSetter() {
        return setter;
    }
    
    public String getGetter() {
        return getter;
    }
    
    public Class<?> getFieldType() {
        return fieldType;
    }
    
    @Override
    public String toString() {
        return "Column(" + columnName + ")";
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
}
