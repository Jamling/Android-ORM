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

import org.junit.Assert;

import cn.ieclipse.aorm.bean.Course;
import cn.ieclipse.aorm.bean.Grade;
import cn.ieclipse.aorm.bean.Person;
import cn.ieclipse.aorm.bean.Student;
import cn.ieclipse.aorm.test.MockDatabase;
import cn.ieclipse.aorm.test.MockOpenHelper;
import junit.framework.TestCase;

/**
 * @author Jamling
 *         
 */
public class SessionTest extends TestCase {
    private Session session;
    private MockDatabase db;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockOpenHelper openHelper = new MockOpenHelper("test.db");
        db = (MockDatabase) openHelper.getReadableDatabase();
        db.addTable("person", Person.class);
        db.addTable("student", Student.class);
        db.addTable("grade", Grade.class);
        db.addTable("course", Course.class);
        session = new Session(openHelper);
        
    }
    
    public void testInsert() {
        Person p = new Person();
        p.id = 10;
        p.name = ("ljm");
        long id = session.insert(p);
        Assert.assertEquals(1, id);
        
        p = session.get(Person.class, id);
        Assert.assertNotNull(p);
        
        Assert.assertEquals(1, p.id);
        Assert.assertEquals("ljm", p.name);
        Assert.assertEquals(0, p.age);
    }
    
    public void testBatchInsert() {
        
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person p = new Person();
            p.id = i + 1;
            p.name = ("ljm" + p.id);
        }
        try {
            session.batchInsert(list);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }
}
