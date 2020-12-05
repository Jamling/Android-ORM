/**
 * 
 */
package cn.ieclipse.aorm.example.bean;

import java.io.Serializable;

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;

/**
 * @author Jamling
 * 
 */
@Table(name = "grade")
public class Grade implements Serializable {
    
    private static final long serialVersionUID = -8976200526604051847L;
    
    private static final int PASS_GRADE = 60;
    
    @Column(name = "_id", id = true)
    private long id;
    
    @Column(name = "_sid")
    private long sid;
    
    @Column(name = "_cid")
    private long cid;
    
    @Column(name = "_score")
    private float score;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getSid() {
        return sid;
    }
    
    public void setSid(long sid) {
        this.sid = sid;
    }
    
    public long getCid() {
        return cid;
    }
    
    public void setCid(long cid) {
        this.cid = cid;
    }
    
    public float getScore() {
        return score;
    }
    
    public void setScore(float score) {
        this.score = score;
    }
    
    public boolean isPass() {
        return this.score >= PASS_GRADE;
    }
}
