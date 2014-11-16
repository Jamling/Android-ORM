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

import junit.framework.TestCase;

import org.junit.Assert;

import cn.ieclipse.aorm.bean.Grade;
import cn.ieclipse.aorm.bean.Student;
import cn.ieclipse.aorm.bean.Undergraduate;
import cn.ieclipse.aorm.test.MockCursor;

/**
 * @author Jamling
 * 
 */
public class CursorUtilsTest extends TestCase {
    
    public void testGetFromCursor() {
        MockCursor cursor = new MockCursor(new String[] { "_id", "_name",
                "_age", "_phone" });
        cursor.addRow(new Object[] { 1, "lijm", 30, "139" });
        List<?> list = CursorUtils.getFromCursor(cursor, Student.class, null);
        Student s = (Student) list.get(0);
        Assert.assertEquals("lijm", s.getName());
        
        // use reflect
        list = CursorUtils.getFromCursorReflect(cursor, Student.class, null);
        s = (Student) list.get(0);
        Assert.assertEquals("lijm", s.getName());
        
        // sub class
        cursor = new MockCursor(new String[] { "_id", "_name", "_age",
                "_phone", "_weight", "_photo" });
        cursor.addRow(new Object[] { 1, "lijm", 30, "139", "1", null });
        list = CursorUtils.getFromCursor(cursor,
                Criteria.create(Undergraduate.class));
        Assert.assertEquals(1, list.size());
        Undergraduate u = (Undergraduate) list.get(0);
        Assert.assertEquals("lijm", u.getName());
        Assert.assertEquals(1f, u.getWeight(), 0f);
        Assert.assertEquals(30, u.getAge());
        
    }
    
    // public void testGetFromCursorMulti() {
    // MockCursor cursor = new MockCursor(new String[] { "s._id", "s._name",
    // "s._balance", "m._pic", "m._mfloat", "m._mffloat", "m._grade",
    // "m._id" });
    // Object[] prepare = new Object[] { 1, "lijm", 2.3f, "pic".getBytes(),
    // 1.0f, new Float(1.1), new Integer(23), 100 };
    // cursor.addRow(prepare);
    // List<Object[]> list = CursorUtils
    // .getFromCursor(cursor, new Class<?>[] { Student.class,
    // Undergraduate.class }, new String[] { "s", "m" });
    // Assert.assertTrue(cursor.getRowSize() == list.size());
    //
    // Object[] actuals = list.get(0);
    // Student s = (Student) actuals[0];
    // Undergraduate m = (Undergraduate) actuals[1];
    //
    // Assert.assertEquals(s.getId(), 1);
    // Assert.assertEquals(s.getName(), "lijm");
    // Assert.assertEquals(s.getBalance(), 2.3f, 0.01);
    //
    // Assert.assertEquals(new String(m.getPic()), "pic");
    // Assert.assertEquals(m.getmFloat(), new Float(1.1f));
    // Assert.assertEquals(m.getId(), 100);
    //
    // // use reflect
    // list = CursorUtils
    // .getFromCursorReflect(cursor, new Class<?>[] { Student.class,
    // Undergraduate.class }, new String[] { "s", "m" });
    // Assert.assertTrue(cursor.getRowSize() == list.size());
    //
    // actuals = list.get(0);
    // s = (Student) actuals[0];
    // m = (Undergraduate) actuals[1];
    //
    // Assert.assertEquals(s.getId(), 1);
    // Assert.assertEquals(s.getName(), "lijm");
    // Assert.assertEquals(s.getBalance(), 2.3f, 0.01);
    //
    // Assert.assertEquals(new String(m.getPic()), "pic");
    // Assert.assertEquals(m.getmFloat(), new Float(1.1f));
    // Assert.assertEquals(m.getId(), 100);
    // }
    
    public void test() {
        MockCursor cursor = new MockCursor(new String[] { "s._id", "s._name",
                "g._cid", "g._score" });
        Object[] prepare = new Object[] { 1, "lijm", new Integer(1), 100 };
        cursor.addRow(prepare);
        
        Criteria criteria = Criteria.create(Student.class, "s")
                .setProjections(new String[] { "id", "name" })
                .addChild(Grade.class, "g")
                .setProjections(new String[] { "cid", "score" })
                .setProjection(true);
        System.out.println(criteria.toSQL());
        Assert.assertArrayEquals(new int[] { 2, 4 },
                criteria.getProjectionSeparators());
        List<Object[]> list = CursorUtils.getFromCursor(cursor,
                criteria.getProjectionClass(),
                criteria.getProjectionSeparators());
        
        Object[] actuals = list.get(0);
        Student s = (Student) actuals[0];
        Grade g = (Grade) actuals[1];
        
        Assert.assertEquals(s.getId(), 1);
        Assert.assertEquals(s.getName(), "lijm");
        Assert.assertEquals(g.getScore(), Float.valueOf(100), 0);
    }
}
