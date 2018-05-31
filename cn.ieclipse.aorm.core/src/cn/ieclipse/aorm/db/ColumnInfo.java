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
package cn.ieclipse.aorm.db;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.aorm.Aorm;
import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.ColumnWrap;
import cn.ieclipse.aorm.annotation.Table;

/**
 * The table column info see <code>PRAGMA TABLE_INFO(tableName);</code>
 * 
 * @author Jamling
 * @date 2017/7/26
 * @since 1.1.4
 */
@Table(name = "SQLITE_COLUMN_INFO")
public class ColumnInfo {
    @Column()
    public int cid;
    @Column()
    public String name;
    @Column()
    public String type;
    @Column()
    public int notnull;
    @Column()
    public String dflt_value;
    @Column()
    public int pk;
    
    public String getDDL() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(' ');
        
        if (pk > 0) {
            sb.append("INTEGER");
            sb.append(' ');
            sb.append("PRIMARY KEY");
        }
        else {
            sb.append(type);
        }
        
        if (notnull > 0) {
            sb.append(" NOT NULL");
        }
        if (dflt_value != null && !dflt_value.isEmpty()) {
            if (dflt_value.startsWith("'") && dflt_value.endsWith("'")) {
            
            }
            else {
                dflt_value = "'" + dflt_value + "'";
            }
            sb.append(" DEFAULT ");
            sb.append(dflt_value);
        }
        return sb.toString();
    }
    
    public boolean same(String name) {
        return this.name.equalsIgnoreCase(name);
    }
    
    @SuppressWarnings("deprecation")
    public static ColumnInfo from(ColumnWrap wrap) {
        ColumnInfo info = new ColumnInfo();
        Column c = wrap.getColumn();
        info.pk = c.id() ? 1 : 0;
        info.notnull = c.notNull() ? 1 : 0;
        info.dflt_value = c.defaultValue();
        String type = wrap.getColumn().type();
        if (type == null || type.isEmpty()) {
            type = Aorm.getMappingFactory()
                    .propertyToColumnType(wrap.getFieldType());
        }
        if (type != null && type.startsWith("java.lang.")) {
            type = type.substring("java.lang.".length());
        }
        info.type = type;
        info.name = wrap.getColumnName();
        return info;
    }
    
    public static List<ColumnInfo> from(List<ColumnWrap> list) {
        int size = list.size();
        List<ColumnInfo> ret = new ArrayList<ColumnInfo>(size);
        for (int i = 0; i < size; i++) {
            ret.add(ColumnInfo.from(list.get(i)));
        }
        return ret;
    }
    
    @Override
    public String toString() {
        return getDDL();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColumnInfo) {
            ColumnInfo another = (ColumnInfo) obj;
            boolean eq = this.getDDL().equals(another.getDDL());
            return eq;
        }
        return super.equals(obj);
    }
}
