# Introduction

Did you used sqlite to save your data on Android? If you did, you may be puzzled for the complexity of mechanism. Now the Andoird ORM (Aorm) coming which armed to make it simple for the developers. If you have the interesting, please join us.

# Features
* Brief ORM mapping, just write an annotation for the field of Java beans. e.g. @Column(name="_name") to mapping _name columnn of database to name property.
* Powerful Forward Engineering supporting, generating DDL and ContentProvider automaticlly.
* Useful Assist feature, create your Activity/Service/BroadcastReceiver with a wizard and configurat them in AndroidManifest.xml automatically.
* ...
More feature, please experience it for your self.

# Projects
## cn.ieclipse.aorm.core
The core project of Android ORM, providing ORM annotation parsing, orm core feature.
## cn.ieclipse.aorm.eclipse
The eclipse plug-in project, provider code generating, DDL generating assist feature. you can found it at https://github.com/Jamling/Aorm-Eclipse-Plugin

## cn.ieclipse.aorm.example
The example Android project using Android ORM.

# Use in Android Studio

```gradle
dependencies {
    compile 'cn.ieclipse.aorm:aorm-core:1.0'
}
```

# Code samples

## Query
```java
Session session = ExampleContentProvider.getSession();
// simplest query, query all student table.
Criteria criteria = Criteria.create(Student.class);
// add restrication: id equals
criteria.add(Restrictions.eq("id", 1));
// add restriction: name like Jamling
criteria.add(Restrictions.like("name", "Jaming"));
// add restriction: age > 30
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
## Author
Jamling 

## Contact 
li.jamling@gmail.com (Always block)

li_jamling@163.com
