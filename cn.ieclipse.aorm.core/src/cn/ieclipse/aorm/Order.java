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

/**
 * @author Jamling
 * 
 */
public class Order {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    
    String property;
    String order;
    
    private Order(String property) {
        this(property, ASC);
    }
    
    private Order(String property, String order) {
        this.property = property;
        this.order = order;
    }
    
    public static Order asc(String property) {
        return new Order(property);
    }
    
    public static Order desc(String property) {
        return new Order(property, DESC);
    }
}
