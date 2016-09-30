/**
 * 
 */
package cn.ieclipse.aorm.bean;

import java.io.Serializable;

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;

/**
 * @author Jamling
 * 
 */
@Table(name = "course")
public class Course implements Serializable {
    
    private static final long serialVersionUID = 4957742859044875650L;
    
    @Column(name = "_id", id = true)
    private long id;
    
    @Column(name = "_name")
    private String name;
    
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
    
}
