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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jamling
 *         
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Column {
    /**
     * column name.
     * 
     * @return name
     */
    public String name() default "";
    
    public String type() default "";
    
    @Deprecated
    public String defaultValue() default "";
    
    public boolean notNull() default false;
    
    public boolean id() default false;
    
    /**
     * The aorm will re-order column based on {@link #order()}. On Android, the
     * {@link Class#getFields()} returns the disordered fields, so the pretty
     * order id, name, age in java bean, may be chaotic name, age, id field
     * order in database. So the {@link #order()} imported to manual order the
     * database field. The smaller {@link #order()} the ahead field order. The
     * {@link #id()} is true column always at the front of other fields.
     * 
     * @return assigned order
     * @since 1.2.0
     */
    public int order() default 0;
}
