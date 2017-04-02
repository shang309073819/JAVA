package cn.itcast.day14;

import java.util.*;

/*
 将自定义对象作为元素存到ArrayList集合中，并去除重复元素。

 比如：存人对象。同姓名同年龄，视为同一个人。为重复元素。

 思路：
 1，对人描述，将数据封装进人对象。
 2，定义容器，将人存入。
 3，取出。

 List集合判断元素是否相同，依据是元素的equals方法。

 */

class Person {
	private String name;
	private int age;

	Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Person))
			return false;
		Person p = (Person) obj;
		return this.name.equals(p.name) && this.age == p.age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", toString()="
				+ super.toString() + "]";
	}

}

class ArrayListTest2 {
	public static void sop(Object obj) {
		System.out.println(obj);
	}

	public static void main(String[] args) {
		ArrayList<Object> al = new ArrayList<Object>();
		al.add(new Person("lisi01", 30));
		al.add(new Person("lisi02", 32));
		al.add(new Person("lisi02", 32));
		al.add(new Person("lisi04", 35));
		al.add(new Person("lisi03", 33));
		al.add(new Person("lisi03", 33));
		al.add(new Person("lisi04", 35));

		al = singleElement(al);

		// remove方法底层也是依赖于元素的equals方法。
		// Removes the first occurrence of the specified element from this list
		sop("remove 03 :" + al.remove(new Person("lisi03", 33)));

		for (Object object : al) {
			System.out.println(object);
		}
	}

	public static ArrayList<Object> singleElement(ArrayList<Object> al) {
		// 定义一个临时容器。
		ArrayList<Object> newAl = new ArrayList<Object>();
		for (Object object : al) {
			if (!newAl.contains(object)) {
				newAl.add(object);
			}
		}
		return newAl;
	}
}
