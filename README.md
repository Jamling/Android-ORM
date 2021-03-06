[![Build Status](https://travis-ci.org/Jamling/Android-ORM.svg?branch=master)](https://travis-ci.org/Jamling/Android-ORM)
[![GitHub release](https://img.shields.io/github/release/jamling/Android-ORM.svg?maxAge=3600)](https://github.com/Jamling/Android-ORM)
[![Bintray](https://img.shields.io/bintray/v/jamling/maven/Android-ORM.svg?maxAge=86400)](https://bintray.com/jamling/maven/Android-ORM)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android_ORM-green.svg?style=flat)](https://android-arsenal.com/details/1/4306)

# Introduction

[简体中文](./README_zh.md)

Did you used `sqlite` to save your data on Android? If you did, you may be puzzled for the complexity of mechanism. Now the Android ORM (Aorm) coming which armed to make it simple for the developers. If you have the interesting, please join us.

# Features

* Brief ORM mapping, only add a `@Column` annotation for the property of Java beans to enable mapping to sqlite table field.

* Native and `ConntentProvider` support, easy to share content, initialize/upgrade database
using Android `ContentProvider`.

* Powerful Forward Engineering supporting, both Eclipse [Android ADT-extensions] and Android Studio [Android ORM Tool] plugin to generate DDL and ContentProvider automatically.

* Useful Assist feature, create your Activity/Service/BroadcastReceiver with a wizard and configure them in AndroidManifest.xml automatically.

* ...

More feature, please experience it for your self.

# Usage

The `$latest` is: [![latest release](https://img.shields.io/github/release/jamling/Android-ORM.svg?maxAge=3600)](https://github.com/Jamling/Android-ORM), **please replace the `$latest` to a concrete version**.

## Use in Eclipse

Put aorm-core-`$latest`.jar to libs/

Recommended to install [Android ADT-extensions] plugin, and add ORM capapility to enable Aorm.

## Use in Android Studio

Aorm has been published to jcenter, so you can just add dependence of aorm in your build.gradle.

```gradle
dependencies {
    compile 'cn.ieclipse.aorm:aorm-core:$latest'
}
```

Recommended to install [Android ORM Tool] plugin to generate code quickly.

# Comparision Results

These are the results for the Simple trial:

![Simple Trial](screenshot/simpletrial.png "Simple Trial")

And these are the results for the Complex trial:

![Complex Trial](screenshot/complextrial.png "Complex Trial")

More detail please see https://github.com/Raizlabs/AndroidDatabaseLibraryComparison

# Code samples

## Create mapping

Simply add `@Table` class annotation and `@Column` field annotation to mapping.

```java
@Table(name = "student")
public class Student implements Serializable {
    
    @Column(name = "_id", id = true)
    public long id; //id is Primary key.
    
    @Column(name="_name", order = 1)
    public String name; //mapping to _name and auto column type
    
    @Column()
    public int age; //auto column type and name
    
    @Column(defaultValue="''")
    public String phone; // default value in empty
    
    public String address; // no mapping
}
```

## Create database

```java
package cn.ieclipse.aorm.example;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import cn.ieclipse.aorm.Aorm;
import cn.ieclipse.aorm.Session;
import cn.ieclipse.aorm.example.bean.Course;
import cn.ieclipse.aorm.example.bean.Grade;
import cn.ieclipse.aorm.example.bean.Student;

/**
 * @author Jamling
 * 
 */
public class ExampleContentProvider extends ContentProvider {
    
    public static final String AUTH = "cn.ieclipse.aorm.example.provider";
    public static final Uri URI = Uri.parse("content://" + AUTH);
    private SQLiteOpenHelper mOpenHelper;
    private static Session session;
    
    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }
    
    @Override
    public String getType(Uri arg0) {
        return null;
    }
    
    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        return null;
    }
    
    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
            String arg4) {
        return null;
    }
    
    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }
    
    public static Session getSession() {
        return session;
    }
    
    @Override
    public boolean onCreate() {
        mOpenHelper = new SQLiteOpenHelper(this.getContext(), "example.db",
                null, 1) {
            public void onCreate(SQLiteDatabase db) {
                // method 3: use AORM to create table
                Aorm.createTable(db, Grade.class);
                Aorm.createTable(db, Student.class);
                Aorm.createTable(db, Course.class);
            }
            
            public void onUpgrade(SQLiteDatabase db, int oldVersion,
                    int newVersion) {
                // update table, suggested to wrapper in if block
                Aorm.updateTable(db, Grade.class);
                Aorm.updateTable(db, Student.class);
                Aorm.updateTable(db, Course.class);
            }
        };
        session = new Session(mOpenHelper, getContext().getContentResolver());
        return true;
    }
    
}
```

## Query

```java
Session session = ExampleContentProvider.getSession();
// simplest query, query all student table.
Criteria criteria = Criteria.create(Student.class);
// add restrication: id equals
criteria.add(Restrictions.eq("id", 1));
// add restriction: name like Jamling
criteria.add(Restrictions.like("name", "Jaming"));
// add restriction: age < 30
criteria.add(Restrictions.lt("age", 30));
// add order
criteria.addOrder(Order.asc("age"));
// set district
criteria.setDistinct(true);
// set limit from row 10 to 20
criteria.setLimit(10, 10);

List<Student> list = session.list(Student.class);
// if you use Android CursorAdapter you can:
Cursor c = session.query(criteria);

// set alias, so the project will be alias.columnn. e.g. s.name
// criteria.setAlias("s");
// multi-table query
criteria.addChild(StudentMore.class, "m", Criteria.INNER_JOIN,
        Restrictions.geProperty("s.id", "m.id"));
// query to cursor
c = session.query(criteria);
// convert to list.
List<Object[]> ret = CursorUtils.getFromCursor(c,
        new Class[] { Student.class }, new String[] { "s", "m" });
// query to list.
ret = session.listAll(criteria);
Object[] item = ret.get(0);
Student s = (Student) item[0];
StudentMore m = (StudentMore) item[1];
//
```

## Other

```java
Session session = ExampleContentProvider.getSession();
// insert
Student s = new Student();
s.setName("Jamling");
long rowId = session.insert(s, null);
// update student's name to Jame whose id is 1
s.setId(1);
s.setName("Jame");
int rows = session.update(s);
// delete student whose id is 2
session.deleteById(Student.class, 2);
// query student whose id is 4
s = session.get(Student.class, 4);
```

# Docs

Refer: http://www.ieclipse.cn/p/Android-ORM/userguide.html

# Author
Jamling Jamling (li.jamling@gmail.com)

[1]: https://img.shields.io/bintray/v/jamling/maven/Android-ORM.svg
[Android ADT-extensions]: https://github.com/Jamling/adt-extensions
[Android ORM Tool]: https://github.com/Jamling/Android-ORM-ASPlugin