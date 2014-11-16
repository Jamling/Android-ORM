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
package cn.ieclipse.aorm.bean;

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;

@Table(name = "undergraduate")
public class Undergraduate extends Student {
    
    private static final long serialVersionUID = 7214955858913570025L;
    
    @Column(name = "_weight")
    private float weight;
    
    private Float oweight;
    
    private double balance;
    
    private Double obalance;
    
    @Column(name = "_photo")
    private byte[] photo;
    
    @Column(name = "_age")
    private int age;
    
    public float getWeight() {
        return weight;
    }
    
    public void setWeight(float weight) {
        this.weight = weight;
    }
    
    public Float getOweight() {
        return oweight;
    }
    
    public void setOweight(Float oweight) {
        this.oweight = oweight;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public Double getObalance() {
        return obalance;
    }
    
    public void setObalance(Double obalance) {
        this.obalance = obalance;
    }
    
    public byte[] getPhoto() {
        return photo;
    }
    
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
}
