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

import cn.ieclipse.aorm.annotation.Column;
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
            sb.append("PRIMARY KEY AUTOINCREMENT");
        }
        else {
            sb.append(type);
        }
        sb.append(notnull > 0 ? " NOT NULL " : "");
        // if (dflt_value != null) {
        // sb.append("DEFAULT '");
        // sb.append(dflt_value);
        // sb.append("'");
        // }
        return sb.toString();
    }
    
    public boolean same(String name) {
        return this.name.equalsIgnoreCase(name);
    }
    
    @Override
    public String toString() {
        return getDDL();
    }
}
