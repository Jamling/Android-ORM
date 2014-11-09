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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Criteria} is a class allowed to custom your query. You can add a child
 * criteria to current criteria.
 * 
 * @author melord
 * 
 */
public class Criteria {
    public static final String LEFT_JOIN = "LEFT JOIN";
    public static final String LEFT_OUTER_JOIN = "LEFT OUTER JOIN";
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String CROSS_JOIN = "CROSS JOIN";
    
    private Class<?> clazz;
    private String table;
    private String alias;
    private List<String> projections = new ArrayList<String>();
    // private List<Restrictions> restrictionList = new
    // ArrayList<Restrictions>();
    private List<Order> orders = new ArrayList<Order>();
    
    private Criteria parent;
    private Criteria child;
    // why root? to improve performance
    private Criteria root;
    
    private Restrictions on;
    private String join = null;
    private boolean resultColumn = false;
    
    // root
    private boolean distinct = false;
    // private List<Restrictions> propRestrictionList = new
    // ArrayList<Restrictions>();
    private List<Object> args = new ArrayList<Object>();
    private Restrictions rootRestrictions;
    private String where;
    // private boolean hasJoin = false;
    private boolean hasLimit = false;
    
    private int pageSize;
    private int offest;
    
    /**
     * Create a criteria as base. If there was no mapping. an ORMException would
     * be throwed.
     * 
     * @param clazz
     *            POJO class mapping to table
     * @return created criteria
     */
    public static Criteria create(Class<?> clazz) {
        return create(clazz, null);
    }
    
    /**
     * Create a root criteria.
     * 
     * 
     * @param clazz
     *            POJO class mapping to table exists.
     * @return created criteria
     */
    public static Criteria create(Class<?> clazz, String alias) {
        Criteria criteria = new Criteria();
        criteria.clazz = clazz;
        criteria.resultColumn = true;
        criteria.root = criteria;
        criteria.table = Mapping.getInstance().getTableName(clazz);
        criteria.alias = alias;
        return criteria;
    }
    
    /**
     * Create a root criteria.
     * 
     * @param clazz
     *            POJO class mapping to table
     * @param alias
     *            if set, projection item and result column would with a
     *            &lt;alias.&gt prefix. Recommended to set alias when
     *            multi-criteria exists.
     * @return created criteria
     */
    public Criteria addChild(Class<?> clazz, String alias) {
        return addChild(clazz, alias, null, null);
    }
    
    /**
     * Add a child criteria to current criteria <br />
     * <strong>Note1:</strong> If null join assigned, will use parent_table as
     * pt, child_table as ct sql;<br />
     * <strong>Note2:</strong> {@link Criteria#LEFT_JOIN Criteria.LEFT_JOIN} ,
     * {@link Criteria#INNER_JOIN Criteria.INNER_JOIN} will not return child
     * table projection unless call {@link #setProjection(boolean)} to true;
     * 
     * @param clazz
     *            The persist object class
     * @param alias
     *            if set, projection item and result column would with a
     *            &lt;alias.&gt prefix. Recommended to set alias when
     *            multi-criteria exists.
     * @param join
     *            join type, use null, {@link Criteria#LEFT_JOIN
     *            Criteria.LEFT_JOIN} , {@link Criteria#INNER_JOIN
     *            Criteria.INNER_JOIN}, {@link Criteria#CROSS_JOIN
     *            Criteria.CROSS_JOIN}, {@link Criteria#LEFT_OUTER_JOIN
     *            Criteria.LEFT_OUTER_JOIN}.
     * 
     * @param on
     *            restriction to join parent criteria
     * @return child criteria
     */
    public Criteria addChild(Class<?> clazz, String alias, String join,
            Restrictions on) {
        Criteria criteria = new Criteria();
        criteria.clazz = clazz;
        criteria.alias = alias;
        criteria.parent = this;
        criteria.root = this.getRoot();
        criteria.join = join;
        criteria.on = on;
        if (criteria.on != null) {
            criteria.on.criteria = criteria;
        }
        criteria.table = Mapping.getInstance().getTableName(clazz);
        criteria.resultColumn = join == null || CROSS_JOIN.equals(join)
                || LEFT_OUTER_JOIN.equals(join);
        
        this.child = criteria;
        return criteria;
    }
    
    /**
     * set criteria alias.if set, projection item and result column would with a
     * &lt;alias.&gt prefix. Recommended to set alias when multi-criteria
     * exists.
     * 
     * @param alias
     *            alias to set
     * @return current criteria
     */
    @Deprecated
    public Criteria setAlias(String alias) {
        this.alias = alias.trim();
        return this;
    }
    
    public Criteria addOrder(Order order) {
        // this.orders.add(order);
        root.orders.add(order);
        return this;
    }
    
    /**
     * Add a restriction to root criteria. If any restrictions set to root
     * criteria, will add "AND" parameter restriction to root restriction.
     * otherwise, use parameter restriction as root restriction.
     * 
     * @param restrictions
     * @return
     */
    public Criteria add(Restrictions restrictions) {
        if (root.rootRestrictions == null) {
            root.rootRestrictions = restrictions;
        }
        else {
            root.rootRestrictions = Restrictions.and(root.rootRestrictions,
                    restrictions);
        }
        root.rootRestrictions.criteria = this;
        return this;
    }
    
    public void setDistinct(boolean distinct) {
        root.distinct = distinct;
    }
    
    /**
     * set query result column projections. if you set alias before, the
     * projections item would add &gt;alias.&lt; as prefix.But you set alias
     * after this, you might can't get expected result. <br />
     * <strong>Note:</strong> the projection is java bean properties array.
     * 
     * 
     * @param projections
     *            property projection, every item would be resolved to result
     *            column, if no column found, {@link ORMException} would be
     *            throw
     * @return current criteria
     */
    public Criteria setProjections(String[] projections) {
        this.projections.clear();
        for (String property : projections) {
            String sub = property;
            if (alias != null && property.startsWith(alias)
                    && property.length() > alias.length()) {
                sub = property.substring(alias.length() + 1);
            }
            String colName = Mapping.getInstance().getColumnName(sub, clazz);
            if (colName == null) {
                throw new ORMException("Mapping Error: no column mapping to "
                        + property);
            }
            if (alias != null) {
                this.projections.add(alias + "." + colName);
            }
            else {
                this.projections.add(colName);
            }
        }
        return this;
    }
    
    /**
     * Set projection columns, the column is the column name of table in
     * database.
     * 
     * @param columns
     *            projection columns
     * @return current criteria
     */
    public Criteria setColumns(String[] columns) {
        this.projections.clear();
        for (int i = 0; i < columns.length; i++) {
            this.projections.add(columns[i]);
        }
        return this;
    }
    
    public Criteria setProjection(boolean projection) {
        this.resultColumn = projection;
        return this;
    }
    
    public Criteria setLimit(int start, int size) {
        root.hasLimit = true;
        root.offest = start;
        root.pageSize = size;
        return this;
    }
    
    public String toSQL() {
        Criteria root = getRoot();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if (root.distinct) {
            sb.append("DISTINCT ");
        }
        concatResultColumn(sb, root);
        sb.append(" FROM ");
        sb.append(root.table);
        if (root.alias != null) {
            sb.append(" AS ");
            sb.append(root.alias);
        }
        concatJoin(sb, root);
        concatWhere(sb, root);
        concatOrder(sb, root);
        concatLimit(sb, root);
        return sb.toString();
    }
    
    public List<Object> getArgs() {
        return root.args;
    }
    
    public String[] getStringArgs() {
        String[] temp = new String[root.args.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = String.valueOf(root.args.get(i));
        }
        return temp;
    }
    
    public String getWhere() {
        return root.where;
    }
    
    public String[] getProjections() {
        List<String> list = new ArrayList<String>();
        Criteria current = root;
        while (current != null) {
            if (current.resultColumn) {
                if (current.projections.isEmpty()) {
                    current.projections = Mapping.getInstance().getColumns(
                            current.alias, current.clazz);
                }
                list.addAll(current.projections);
            }
            current = current.child;
        }
        return list.toArray(new String[] {});
    }
    
    Criteria getRoot() {
        Criteria root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }
    
    Class<?> getClazz() {
        return clazz;
    }
    
    String getAlias() {
        return alias;
    }
    
    private void concatResultColumn(StringBuilder sb, Criteria root) {
        Criteria current = root;
        while (current != null) {
            if (current.resultColumn) {
                if (current.projections.isEmpty()) {
                    current.projections = Mapping.getInstance().getColumns(
                            current.alias, current.clazz);
                }
                for (String col : current.projections) {
                    sb.append(col);
                    sb.append(", ");
                }
            }
            current = current.child;
        }
        sb.delete(sb.length() - 2, sb.length());
    }
    
    private void concatJoin(StringBuilder sb, Criteria root) {
        Criteria current = root.child;
        while (current != null) {
            if (current.join == null) {
                sb.append(",");
            }
            else {
                sb.append(" ");
                sb.append(current.join);
            }
            sb.append(" ");
            // TODO TABLE NAME
            // sb.append(Cache.getInstance().getTableName(current.clazz));
            sb.append(current.table);
            if (current.alias != null) {
                sb.append(" AS ");
                sb.append(current.alias);
            }
            if (current.on != null) {
                sb.append(" ON ");
                // sb.append(on.toString());
                sb.append(current.on.getWhere(null));
            }
            current = current.child;
        }
    }
    
    private void concatWhere(StringBuilder sb, Criteria root) {
        // boolean appendWhere = false;
        root.args.clear();
        if (root.rootRestrictions != null) {
            root.where = root.rootRestrictions.getWhere(root.args);
            if (root.where.length() > 0) {
                sb.append(" WHERE ");
                sb.append(root.where);
            }
        }
        // Criteria current = root;
        // for (Restrictions restriction : current.propRestrictionList) {
        // if (!appendWhere) {
        // sb.append(" WHERE");
        // appendWhere = true;
        // }
        // sb.append(" ");
        // sb.append(property2Column(restriction.property));
        // sb.append(restriction.op);
        // sb.append(property2Column((String) restriction.value));
        // }
        // while (current != null) {
        // if (!current.restrictionList.isEmpty()) {
        // if (!appendWhere) {
        // sb.append(" WHERE");
        // appendWhere = true;
        // }
        // for (Restrictions restriction : current.restrictionList) {
        // sb.append(" ");
        // sb.append(property2Column(restriction.property));
        // sb.append(restriction.op);
        // sb.append('?');
        // root.args.add(restriction.value);
        // }
        // }
        // current = current.child;
        // }
        
    }
    
    private void concatOrder(StringBuilder sb, Criteria root) {
        if (!root.orders.isEmpty()) {
            sb.append(" ORDER BY ");
            int i = 0;
            for (Order order : root.orders) {
                sb.append(property2Column(order.property));
                sb.append(" ");
                sb.append(order.order);
                if (++i < root.orders.size()) {
                    sb.append(", ");
                }
            }
        }
    }
    
    private void concatLimit(StringBuilder sb, Criteria root) {
        if (hasLimit) {
            sb.append(" LIMIT ");
            sb.append(root.pageSize);
            sb.append(" OFFSET ");
            sb.append(root.offest);
        }
    }
    
    String property2Column(String property) {
        Criteria current = root;
        String ret = null;
        String str1 = "";
        String str2 = property;
        int pos = property.indexOf('.');
        // exist alias
        
        if (pos >= 0 && pos + 1 < str2.length()) {
            str1 = property.substring(0, pos);
            str2 = property.substring(pos + 1);
            
            while (current != null) {
                if (str1.equals(current.alias)) {
                    String colName = Mapping.getInstance().getColumnName(str2,
                            current.clazz);
                    if (colName == null) {
                        throw new ORMException(
                                "Mapping Error: No such maping for "
                                        + property
                                        + ", did you written Column annotation before "
                                        + str2 + " in "
                                        + current.clazz.getName());
                    }
                    str2 = colName;
                    break;
                }
                current = current.child;
            }
            ret = str1 + "." + str2;
        }
        else {
            boolean map = false;
            for (; current != null; current = current.child) {
                String colName = Mapping.getInstance().getColumnName(str2,
                        current.clazz);
                if (colName == null) {
                    continue;
                }
                str2 = colName;
                if (current.alias != null) {
                    str2 = current.alias + "." + colName;
                }
                map = true;
                break;
            }
            if (!map) {
                throw new ORMException("Mapping Error: No such maping for "
                        + property);
            }
            ret = str2;
        }
        
        return ret;
    }
    
    // String column2Property(String column) {
    // Criteria current = root;
    // String ret = null;
    // String str1 = "";
    // String str2 = column;
    // int pos = column.indexOf('.');
    // exist alias
    
    // if (pos >= 0 && pos + 1 < str2.length()) {
    // str1 = column.substring(0, pos);
    // str2 = column.substring(pos + 1);
    // while (current != null) {
    // if (str1.equals(current.alias)) {
    // String prop = Cache.getInstance().getPropertyName(str2,
    // clazz);
    //
    // }
    // }
    // }
    // return ret;
    // }
    
    /**
     * get criteria projection classes.
     * 
     * @return
     */
    Class<?>[] getProjectionClass() {
        ArrayList<Class<?>> list = new ArrayList<Class<?>>(1);
        Criteria root = this.root;
        while (root != null) {
            if (root.resultColumn) {
                list.add(root.clazz);
            }
            root = root.child;
        }
        return list.toArray(new Class[list.size()]);
    }
    
    /**
     * get projection class separator array.
     * 
     * @return
     */
    int[] getProjectionSeparators() {
        int[] src = new int[10];
        Criteria root = this.root;
        int i = 0;
        int sum = 0;
        while (root != null) {
            if (root.resultColumn) {
                sum += root.projections.size();
                src[i++] = sum;
            }
            root = root.child;
        }
        int[] dst = new int[i];
        System.arraycopy(src, 0, dst, 0, i);
        return dst;
    }
    
    /**
     * get property ' column name, may with a alias.
     * 
     * @param properties
     * @return
     */
    public String[] getColumns(String[] properties) {
        String[] dst = new String[properties.length];
        int i = 0;
        for (String string : properties) {
            dst[i++] = getProjectionColumn(string);
        }
        return dst;
    }
    
    private String getProjectionColumn(String property) {
        String column = property;
        int pos = property.indexOf('.');
        if (pos > 0 && pos + 1 < property.length()) {
            String alias = property.substring(0, pos);
            String sub = property.substring(pos + 1);
            Criteria current = this.root;
            while (current != null) {
                if (alias.equals(current.alias)) {
                    column = Mapping.getInstance().getColumnName(sub,
                            current.clazz);
                    break;
                }
                current = current.child;
            }
        }
        else {
            column = Mapping.getInstance().getColumnName(property, root.clazz);
        }
        if (column == null) {
            column = property;
        }
        return column;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "(" + getClazz() + ", alias "
                + getAlias() + ")";
    }
}
