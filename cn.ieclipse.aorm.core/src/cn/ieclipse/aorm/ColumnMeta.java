/*
 * Copyright 2014-2015 ieclipse.cn.
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

import cn.ieclipse.aorm.annotation.ColumnWrap;
import cn.ieclipse.aorm.db.ColumnInfo;

/**
 * Column annotation meta-data
 * 
 * @author Jamling
 * @deprecated use {@link ColumnInfo} instead
 */
public class ColumnMeta {
    String name;
    String type;
    
    boolean haveDefaultValue;
    String defaultValue;
    
    boolean haveNotNull;
    boolean notNull;
    
    boolean haveId;
    boolean id;
    
    ColumnInfo columnInfo;
    
    public ColumnMeta() {
    
    }
    
    public ColumnMeta(ColumnWrap wrap) {
        this.columnInfo = ColumnInfo.from(wrap);
    }
    
    public String toSQL() {
        return columnInfo.getDDL();
    }
}
