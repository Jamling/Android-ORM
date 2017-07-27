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

/**
 * The mapping factory
 * 
 * @author Jamling
 * @date 2017/07/27
 *       
 * @since 1.1.4
 */
public abstract class MappingFactory {
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
}
