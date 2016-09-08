[![Build Status](https://travis-ci.org/Jamling/Android-ORM.svg?branch=master)](https://travis-ci.org/Jamling/Android-ORM)
[![GitHub release](https://img.shields.io/github/release/jamling/Android-ORM.svg?maxAge=3600)](https://github.com/Jamling/Android-ORM)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android_ORM-green.svg?style=flat)](https://android-arsenal.com/details/1/4306)

# 简介

您是否在Android上使用sqlite来存储您的应用数据呢？如果是，或许您会对Android存取sqlite数据感觉有点小复杂。本人从2010年开始从事Android开发，对于Android的数据库操纵这一块，感觉还是比较复杂的，尤其是可维护性及或可扩展性方面，都是比较差的。
也正是从这时开始，我决定在Android平台上实现像Hibernate那样的ORM框架，来简化开发工作。使得开发者在使用sqlite数据库时，无需关注底层数据库实现，更多的是关注上层业务逻辑实现。
如果您对此项目感兴趣，欢迎您的加入！

# 特性
* 简洁的ORM映射配置, 只需在Java Bean的属性中添加类似@Column(name="_name")的注解即可完成到数据库字段的映射配置.
* 强大的正向工程支持, 自动生成DDL和ContentProvider.
* 给力的辅助工具, 向导式新建Activity/Service/BroadcastReceiver并且自动配置到AndroidManifest.xml.
* ...
更多特性，请自行体验.

# 使用
## Eclipse

下载aorm-core-1.0.jar并放入libs/目录下

推荐安装[Android ADT-extensions](https://github.com/Jamling/adt-extensions/)插件

## Android Studio
Aorm已经发布到jcenter，在您的app/build.gradle中添加以下依赖。

```gradle
dependencies {
    compile 'cn.ieclipse.aorm:aorm-core:1.1.0'
}
```

# 示例代码

## 查询
```java
        Session session = ExampleContentProvider.getSession();
        // 最简单的查询：查询所有的学生信息.
        Criteria criteria = Criteria.create(Student.class);
        // 添加条件: id相等
        criteria.add(Restrictions.eq("id", 1));
        // 添加条件: name like Jamling
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
## 其它操作
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

# 文档
请参考http://ieclipse.cn/p/Android-ORM/userguide.html

# 作者
Jamling (li.jamling@gmail.com)
