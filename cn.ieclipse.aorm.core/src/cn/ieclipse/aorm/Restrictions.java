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

import java.util.List;

/**
 * @author Jamling
 * 
 */
public class Restrictions {
    protected final static String EQ = "=";
    
    protected final static String NE = "!=";
    
    protected final static String LT = "<";
    
    protected final static String GT = ">";
    
    protected final static String LTEQ = "<=";
    
    protected final static String GTEQ = ">=";
    
    protected final static String LIKE = "LIKE";
    
    protected final static String IS = "IS";
    
    protected final static String NULL = "NULL";
    
    protected final static String NOT_NULL = "NOT NULL";
    
    protected final static String BETWEEN = "BETWEEN";
    
    String op;
    
    String property;
    
    Object value;
    
    Restrictions left;
    
    String middle;
    
    Restrictions right;
    
    Criteria criteria;
    
    private Restrictions(String op, String property, Object value) {
        this.op = op;
        this.property = property;
        this.value = value;
    }
    
    private Restrictions(Restrictions left, String middle, Restrictions right) {
        this.left = left;
        // this.middle = middle;
        this.op = middle;
        this.right = right;
    }
    
    // equals
    /**
     * property equals a value.
     * 
     * @param property
     *            property name
     * @param value
     *            excepted value
     * @return equals restriction
     */
    public static Restrictions eq(String property, Object value) {
        return new Restrictions(EQ, property, value);
    }
    
    public static Restrictions eqProperty(String property1, String property2) {
        return new PropertyRestrictions(EQ, property1, property2);
    }
    
    public static Restrictions ne(String property, Object value) {
        return new Restrictions(NE, property, value);
    }
    
    public static Restrictions neProperty(String property1, String property2) {
        return new PropertyRestrictions(NE, property1, property2);
    }
    
    // greater than >
    public static Restrictions gt(String property, Object value) {
        return new Restrictions(GT, property, value);
    }
    
    public static Restrictions gtProperty(String property1, String property2) {
        return new PropertyRestrictions(GT, property1, property2);
    }
    
    // greater equals >=
    public static Restrictions ge(String property, Object value) {
        return new Restrictions(GTEQ, property, value);
    }
    
    public static Restrictions geProperty(String property1, String property2) {
        return new PropertyRestrictions(GTEQ, property1, property2);
    }
    
    // less than <
    public static Restrictions lt(String property, Object value) {
        return new Restrictions(LT, property, value);
    }
    
    public static Restrictions ltProperty(String property1, String property2) {
        return new PropertyRestrictions(LT, property1, property2);
    }
    
    // less equals <=
    public static Restrictions le(String property, Object value) {
        return new Restrictions(LTEQ, property, value);
    }
    
    public static Restrictions leProperty(String property1, String property2) {
        return new PropertyRestrictions(LTEQ, property1, property2);
    }
    
    public static Restrictions isNull(String property) {
        return new IsRestrictions(IS, property, NULL);
    }
    
    public static Restrictions isNotNull(String property) {
        return new IsRestrictions(IS, property, NOT_NULL);
    }
    
    public static Restrictions like(String property, Object value) {
        return new LikeRestrictions(LIKE, property, value);
    }
    
    public static Restrictions between(String property, Object value,
            Object rightValue) {
        return new BetweenRestrictions(BETWEEN, property, value, rightValue);
    }
    
    // ----------->
    public static Restrictions or(Restrictions left, Restrictions right) {
        return new Restrictions(left, "OR", right);
    }
    
    public static Restrictions and(Restrictions left, Restrictions right) {
        return new Restrictions(left, "AND", right);
    }
    
    @Override
    public String toString() {
        return property + op + value;
    }
    
    protected String toLeftSQL() {
        String colName = criteria.property2Column(property);
        // if(criteria.)
        return colName;
    }
    
    protected String toMiddleSQL() {
        return " " + op + " ";
    }
    
    protected String toRightSQL(List<Object> args) {
        args.add(value);
        return "?";
    }
    
    final String getWhere(List<Object> args) {
        StringBuilder sb = new StringBuilder();
        walk(this, sb, args);
        return sb.toString();
    }
    
    final void walk(Restrictions res, StringBuilder sb, List<Object> args) {
        if (res.left != null) {
            res.left.criteria = res.criteria;
            sb.append("(");
            walk(res.left, sb, args);
        }
        else {
            // String col = res.criteria.property2Column(res.property);
            // sb.append(col);
            sb.append(res.toLeftSQL());
        }
        // sb.append(res.op);
        sb.append(res.toMiddleSQL());
        if (res.right != null) {
            res.right.criteria = res.criteria;
            walk(res.right, sb, args);
            sb.append(")");
        }
        else {
            // if (res instanceof PropertyRestrictions) {
            // String col = res.criteria.property2Column((String) res.value);
            // sb.append(col);
            // } else {
            // sb.append('?');
            // args.add(res.value);
            // }
            sb.append(res.toRightSQL(args));
        }
    }
    
    static class PropertyRestrictions extends Restrictions {
        private PropertyRestrictions(String op, String property, Object value) {
            super(op, property, value);
        }
        
        protected String toRightSQL(List<Object> args) {
            return criteria.property2Column((String) value);
        }
    }
    
    static class LikeRestrictions extends Restrictions {
        private LikeRestrictions(String op, String property, Object value) {
            super(op, property, value);
        }
        
        protected String toRightSQL(List<Object> args) {
            args.add(value);
            return "?";
        }
    }
    
    static class IsRestrictions extends Restrictions {
        private IsRestrictions(String op, String property, Object value) {
            super(op, property, value);
        }
        
        protected String toRightSQL(List<Object> args) {
            return (String) value;
        }
    }
    
    static class BetweenRestrictions extends Restrictions {
        private Object rightValue;
        
        private BetweenRestrictions(String op, String property, Object value,
                Object rightValue) {
            super(op, property, value);
            this.rightValue = rightValue;
        }
        
        protected String toRightSQL(List<Object> args) {
            args.add(value);
            args.add(rightValue);
            return "? AND ?";
        }
    }
}
