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

import java.util.List;

import org.junit.After;
import org.junit.Before;

import cn.ieclipse.aorm.annotation.ColumnWrap;
import cn.ieclipse.aorm.annotation.TableWrap;
import cn.ieclipse.aorm.bean.FullBean;
import junit.framework.TestCase;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2016年9月29日
 *       
 */
public class ColumnMetaTest extends TestCase {
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    public void testDbColType() {
        TableWrap table = new TableWrap(FullBean.class);
        List<ColumnWrap> cols = table.getColumnWraps();
        for (int i = 0; i < cols.size(); i++) {
            ColumnMeta meta = new ColumnMeta(cols.get(i));
//            System.out.println(meta.name);
//            System.out.println(meta.type);
            System.out.println(meta.toSQL());
        }
    }
    
}
