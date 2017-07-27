/**
 * 
 */
package cn.ieclipse.aorm.bean;

import java.io.Serializable;
import java.util.List;

import cn.ieclipse.aorm.Mapping;
import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;

/**
 * @author Jamling
 * 
 */
@Table(name = "student")
public class Student implements Serializable {
    
    private static final long serialVersionUID = 8010508999597447226L;
    
    @Column(name = "_id", id = true)
    public long id;
    
    @Column(name="_name")
    public String name;
    
    @Column(name = "_age")
    public int age;
    
    @Column(name = "_phone")
    public String phone;
    
    public String address;
    
//    public long getId() {
//        return id;
//    }
//    
//    public void setId(long id) {
//        this.id = id;
//    }
//    
//    public String getName() {
//        return name;
//    }
//    
//    public void setName(String name) {
//        this.name = name;
//    }
//    
//    public int getAge() {
//        return age;
//    }
//    
//    public void setAge(int age) {
//        this.age = age;
//    }
//    
//    public String getPhone() {
//        return phone;
//    }
//    
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//    
//    public String getAddress() {
//        return address;
//    }
//    
//    public void setAddress(String address) {
//        this.address = address;
//    }
    public static void main(String[] args) {
        List<String> cols = Mapping.getInstance().getColumns(null, Student.class);
        System.out.println(cols);
    }
}
