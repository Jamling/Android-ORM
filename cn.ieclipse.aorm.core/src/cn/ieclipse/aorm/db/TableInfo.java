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
 * The table info. <code>select * from sqlite_master where type='table' </code>
 * 
 * @author Jamling
 * @date 2017/7/26
 * @since 1.1.4
 *        
 */
@Table(name = "SQLITE_TABLE_INFO")
public class TableInfo {
    @Column
    public String type;
    @Column
    public String name;
    @Column
    public String tbl_name;
    @Column
    public int rootpage;
    @Column
    public String sql;
    
    public boolean same(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
