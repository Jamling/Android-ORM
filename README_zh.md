# ���

���Ƿ���Android��ʹ��sqlite���洢����Ӧ�������أ�����ǣ����������Android��ȡsqlite���ݸо��е�С���ӡ����˴�2010�꿪ʼ����Android����������Android�����ݿ������һ�飬�о����ǱȽϸ��ӵģ������ǿ�ά���Լ������չ�Է��棬���ǱȽϲ�ġ�
Ҳ���Ǵ���ʱ��ʼ���Ҿ�����Androidƽ̨��ʵ����Hibernate������ORM��ܣ����򻯿���������ʹ�ÿ�������ʹ��sqlite���ݿ�ʱ�������ע�ײ����ݿ�ʵ�֣�������ǹ�ע�ϲ�ҵ���߼�ʵ�֡�
������Դ���Ŀ����Ȥ����ӭ���ļ��룡

# ����
* ����ORMӳ������, ֻ����Java Bean���������������@Column(name="_name")��ע�⼴����ɵ����ݿ��ֶε�ӳ������.
* ǿ������򹤳�֧��, �Զ�����DDL��ContentProvider.
* �����ĸ�������, ��ʽ�½�Activity/Service/BroadcastReceiver�����Զ����õ�AndroidManifest.xml.
* ...
�������ԣ�����������.

# ����
## cn.ieclipse.aorm.core
Android ORM���Ĺ���, �ṩORMע������Ⱥ��Ĺ���.
## cn.ieclipse.aorm.eclipse
������eclipse�������,�ṩ�����Զ����ɣ�DDL���ɵȸ�������.
## cn.ieclipse.aorm.example
ʹ��AORM��ʾ��AndroidӦ�ù���.

# ��װ��ʹ��
## ���ؼ���װAndroid ORM���
Eclipse 3.6���Ͽ���ͨ��Help->Marketplace�򿪲���г���ͨ�������ؼ����ҵ���������أ�Ҳ�������ز�����ذ�װ����װ���

## 

# ʾ������

## ��ѯ

        Session session = ExampleContentProvider.getSession();
        // ��򵥵Ĳ�ѯ����ѯ���е�ѧ����Ϣ.
        Criteria criteria = Criteria.create(Student.class);
        // �������: id���
        criteria.add(Restrictions.eq("id", 1));
        // �������: name like Jamling
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

## ��������
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

## ����
Jamling 

## Contact 
li.jamling@gmail.com (Always block)

li_jamling@163.com
