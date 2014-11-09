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

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import cn.ieclipse.aorm.bean.Person;
import cn.ieclipse.aorm.bean.Student;

/**
 * @author Jamling
 * 
 */
public class CriteriaTest extends TestCase {
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    public void testCreate() {
        Criteria criteria = Criteria.create(Person.class);
        String expected = "SELECT _id, _name, _age FROM person";
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testCreateWithAlias() {
        Criteria criteria = Criteria.create(Person.class, "p");
        String expected = "SELECT p._id, p._name, p._age FROM person AS p";
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testAddChild() {
        Criteria criteria = Criteria.create(Person.class, "p");
        criteria.addChild(Student.class, "s");
        String expected = "SELECT p._id, p._name, p._age, s._id, s._name, s._age, s._phone FROM person AS p, student AS s";
        // System.out.println(expected);
        // System.out.println(criteria.toSQL());
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testAddChildWithJoin() {
        // cross join;
        Criteria criteria = Criteria.create(Person.class, "p");
        criteria.addChild(Student.class, "s", Criteria.CROSS_JOIN, null);
        String expected = "SELECT p._id, p._name, p._age, s._id, s._name, s._age, s._phone FROM person AS p CROSS JOIN student AS s";
        // System.out.println(expected);
        // System.out.println(criteria.toSQL());
        Assert.assertEquals(expected, criteria.toSQL());
        
        // use on;
        criteria = Criteria.create(Person.class, "p");
        criteria.addChild(Student.class, "s", Criteria.CROSS_JOIN,
                Restrictions.eqProperty("p.age", "s.age"));
        expected = "SELECT p._id, p._name, p._age, s._id, s._name, s._age, s._phone FROM person AS p CROSS JOIN student AS s ON p._age = s._age";
        // System.out.println(expected);
        // System.out.println(criteria.toSQL());
        Assert.assertEquals(expected, criteria.toSQL());
        
        // use left join;
        criteria = Criteria.create(Person.class, "p");
        criteria.addChild(Student.class, "s", Criteria.LEFT_JOIN,
                Restrictions.eqProperty("p.age", "s.age"));
        expected = "SELECT p._id, p._name, p._age FROM person AS p LEFT JOIN student AS s ON p._age = s._age";
        
        Assert.assertEquals(expected, criteria.toSQL());
        
        // use left join with projection;
        criteria = Criteria.create(Person.class, "p");
        criteria.addChild(Student.class, "s", Criteria.LEFT_JOIN,
                Restrictions.eqProperty("p.age", "s.age")).setProjection(true);
        expected = "SELECT p._id, p._name, p._age, s._id, s._name, s._age, s._phone FROM person AS p LEFT JOIN student AS s ON p._age = s._age";
        
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testAddOrder() {
        Criteria criteria = Criteria.create(Person.class, "p");
        criteria.addOrder(Order.desc("p.age"));
        String expected = "SELECT p._id, p._name, p._age FROM person AS p ORDER BY p._age DESC";
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testSetDistinct() {
        Criteria criteria = Criteria.create(Person.class, "p");
        criteria.setDistinct(true);
        String expected = "SELECT DISTINCT p._id, p._name, p._age FROM person AS p";
        System.out.println(criteria.toSQL());
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testSetProjections() {
        Criteria criteria = Criteria.create(Person.class).setProjections(
                new String[] { "name" });
        String expected = "SELECT _name FROM person";
        Assert.assertEquals(expected, criteria.toSQL());
        
        criteria = Criteria.create(Person.class, "p").setProjections(
                new String[] { "id", "name" });
        expected = "SELECT p._id, p._name FROM person AS p";
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testSetColumns() {
        Criteria criteria = Criteria.create(Person.class).setColumns(
                new String[] { "_name" });
        String expected = "SELECT _name FROM person";
        Assert.assertEquals(expected, criteria.toSQL());
        
        criteria = Criteria.create(Person.class, "p").setColumns(
                new String[] { "p._id", "p._name" });
        expected = "SELECT p._id, p._name FROM person AS p";
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
    public void testSetLimit() {
        Criteria criteria = Criteria.create(Student.class)
                .add(Restrictions.eq("name", "lijm"))
                .add(Restrictions.eq("id", 34)).addOrder(Order.asc("id"))
                .addOrder(Order.desc("name")).setLimit(0, 20);
        String expected = "SELECT _id, _name, _age, _phone FROM student WHERE (_name = ? AND _id = ?) ORDER BY _id ASC, _name DESC LIMIT 20 OFFSET 0";
        Assert.assertEquals(expected, criteria.toSQL());
    }
    
}
