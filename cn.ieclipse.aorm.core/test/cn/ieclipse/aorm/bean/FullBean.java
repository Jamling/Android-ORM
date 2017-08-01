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
package cn.ieclipse.aorm.bean;

import cn.ieclipse.aorm.annotation.Column;
import cn.ieclipse.aorm.annotation.Table;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2016/09/29
 *       
 */
@Table(name = "test")
public class FullBean {
    @Column(name = "_id", id = true)
    public int id;
    
    @Column()
    public Integer oid;
    
    @Column()
    public long size;
    
    @Column()
    public Long osize;
    @Column()
    public short age;
    @Column()
    public Short oage;
    @Column()
    public boolean exist;
    @Column()
    public Boolean oexist;
    @Column()
    public float weight;
    @Column()
    public Float oweight;
    @Column()
    public double balance;
    @Column()
    public Double obalance;
    @Column()
    public byte[] blob;
    @Column()
    public Byte[] oblob;
    @Column()
    public String address;
}
