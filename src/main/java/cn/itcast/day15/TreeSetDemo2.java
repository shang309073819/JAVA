package cn.itcast.day15;

import java.util.*;

/*
 当元素自身不具备比较性，或者具备的比较性不是所需要的。
 这时需要让容器自身具备比较性。
 定义了比较器，将比较器对象作为参数传递给TreeSet集合的构造函数。
 当两种排序都存在时，以比较器为主。
 定义一个类，实现Comparator接口，覆盖compare方法。

 */

class Student2 implements Comparable// 该接口强制让学生具备比较性。
		<Object> {
	private String name;
	private int age;

	Student2(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public int compareTo(Object obj) {

		// return 0;

		if (!(obj instanceof Student2))
			throw new RuntimeException("不是学生对象");
		Student2 s = (Student2) obj;

		// System.out.println(this.name+"....compareto....."+s.name);
		if (this.age > s.age)
			return 1;
		if (this.age == s.age) {
			return this.name.compareTo(s.name);
		}
		return -1;
		/**/
	}

	public String getName() {
		return name;

	}

	public int getAge() {
		return age;
	}
}

class TreeSetDemo2 {
	public static void main(String[] args) {
		TreeSet<Student2> ts = new TreeSet<Student2>(new MyCompare());

		ts.add(new Student2("lisi02", 22));
		ts.add(new Student2("lisi02", 21));
		ts.add(new Student2("lisi007", 20));
		ts.add(new Student2("lisi09", 19));
		ts.add(new Student2("lisi06", 18));
		ts.add(new Student2("lisi06", 18));
		ts.add(new Student2("lisi007", 29));
		ts.add(new Student2("lisi007", 20));
		ts.add(new Student2("lisi01", 40));

		for (Student2 student2 : ts) {
			System.out.println(student2.getName() + "..." + student2.getAge());
		}
	}
}

class MyCompare implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		Student2 s1 = (Student2) o1;
		Student2 s2 = (Student2) o2;

		int num = s1.getName().compareTo(s2.getName());
		if (num == 0) {
			return new Integer(s1.getAge()).compareTo(new Integer(s2.getAge()));
		}

		return num;

	}
}