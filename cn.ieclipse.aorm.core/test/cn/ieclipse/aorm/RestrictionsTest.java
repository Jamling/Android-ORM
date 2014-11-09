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

import org.junit.Assert;

import cn.ieclipse.aorm.bean.Grade;
import cn.ieclipse.aorm.bean.Student;

/**
 * @author Jamling
 * 
 */
public class RestrictionsTest extends TestCase {
    
    public void testAnd() {
        Criteria criteria = Criteria.create(Student.class);
        criteria.add(Restrictions.and(
                Restrictions.or(Restrictions.eq("name", "lijm"),
                        Restrictions.eq("name", "ljm")),
                Restrictions.or(Restrictions.eq("id", 2),
                        Restrictions.eqProperty("id", "name"))));
        String expected = "((_name = ? OR _name = ?) AND (_id = ? OR _id = _name))";
        String sql = criteria.toSQL();
        Assert.assertEquals(expected, criteria.getWhere());
        Assert.assertArrayEquals(new Object[] { "lijm", "ljm", 2 }, criteria
                .getArgs().toArray());
    }
    
    public void testOr() {
        Criteria criteria = Criteria.create(Student.class);
        criteria.add(
                Restrictions.or(Restrictions.eq("name", "lijm"),
                        Restrictions.eq("name", "ljm")))
                .add(Restrictions.eqProperty("id", "name")).toSQL();
        String expected = "((_name = ? OR _name = ?) AND _id = _name)";
        Assert.assertEquals(expected, criteria.getWhere());
        
        Assert.assertArrayEquals(new Object[] { "lijm", "ljm" }, criteria
                .getArgs().toArray());
    }
    
    public void testIs() {
        Criteria criteria = Criteria.create(Student.class)
                .add(Restrictions.isNull("name"))
                .add(Restrictions.isNotNull("phone"));
        String sql = criteria.toSQL();
        String expected = "(_name IS NULL AND _phone IS NOT NULL)";
        Assert.assertEquals(expected, criteria.getWhere());
    }
    
    public void testBetween() {
        Criteria criteria = Criteria.create(Student.class)
                .add(Restrictions.isNull("name"))
                .add(Restrictions.between("age", 10, 20));
        String sql = criteria.toSQL();
        String expected = "(_name IS NULL AND _age BETWEEN ? AND ?)";
        Assert.assertEquals(expected, criteria.getWhere());
        Assert.assertArrayEquals(new Object[] { 10, 20 }, criteria.getArgs()
                .toArray());
    }
    
    public void testEq() {
        Criteria criteria = Criteria.create(Student.class).add(
                Restrictions.eq("id", 2));
        String sql = criteria.toSQL();
        String expected = "_id = ?";
        Assert.assertEquals(expected, criteria.getWhere());
        Assert.assertArrayEquals(new Object[] { 2 }, criteria.getArgs()
                .toArray());
    }
    
    public void testEqProperty() {
        Criteria criteria = Criteria.create(Student.class, "s")
                .add(Restrictions.eq("id", 2)).addChild(Grade.class, "g")
                .add(Restrictions.eqProperty("s.id", "g.sid"));
        String sql = criteria.toSQL();
        String expected = "(s._id = ? AND s._id = g._sid)";
        Assert.assertEquals(expected, criteria.getWhere());
        Assert.assertArrayEquals(new Object[] { 2 }, criteria.getArgs()
                .toArray());
    }
    
}
