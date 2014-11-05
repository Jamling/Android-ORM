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

/**
 * @author Jamling
 * 
 */
public class ColumnWrap {
    private Column column;
    private Field field;
    private String getter;
    private String setter;
    private Class<?> fieldType;
    
    public ColumnWrap(Column column, Field field) {
        this.column = column;
        this.field = field;
        fieldType = (Class<?>) field.getGenericType();
        
        String fieldName = field.getName();
        if (boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
            if (fieldName.startsWith("is") && fieldName.length() > 2) {
                fieldName = fieldName.substring(2);
            }
            getter = "is" + capitalize(fieldName);
        }
        else {
            getter = "get" + capitalize(fieldName);
        }
        
        setter = "set" + capitalize(fieldName);
    }
    
    public String getPropertyName() {
        return field.getName();
    }
    
    public String getColumnName() {
        return column.name();
    }
    
    public Field getField() {
        return field;
    }
    
    public Column getColumn() {
        return column;
    }
    
    // public void setSetter(String setter) {
    // this.setter = setter;
    // }
    
    public String getSetter() {
        return setter;
    }
    
    // public void setGetter(String getter) {
    // this.getter = getter;
    // }
    
    public String getGetter() {
        return getter;
    }
    
    public Class<?> getFieldType() {
        return fieldType;
    }
    
    @Override
    public String toString() {
        return "Column(" + column.name() + ")";
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
