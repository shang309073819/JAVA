package cn.itcast.day15;

import java.util.*;

/*
 |--Set：元素是无序(存入和取出的顺序不一定一致)，元素不可以重复。
 |--HashSet:底层数据结构是哈希表。是线程不安全的。不同步。
 HashSet是如何保证元素唯一性的呢？
 是通过元素的两个方法，hashCode和equals来完成。
 如果元素的HashCode值相同，才会判断equals是否为true。
 如果元素的hashcode值不同，不会调用equals。

 注意,对于判断元素是否存在，以及删除等操作，依赖的方法是元素的hashcode和equals方法。

 Set集合的功能和Collection是一致的。
 */

/*
 往hashSet集合中存入自定对象
 姓名和年龄相同为同一个人，重复元素。
 */
class HashSetTest {
	public static void sop(Object obj) {
		System.out.println(obj);
	}

	public static void main(String[] args) {
		HashSet<Person2> hs = new HashSet<Person2>();

		hs.add(new Person2("a1", 11));
		hs.add(new Person2("a2", 12));
		hs.add(new Person2("a3", 13));
		hs.add(new Person2("a2", 12));
		hs.add(new Person2("a4", 14));

		sop("a1:" + hs.contains(new Person2("a2", 12)));

		hs.remove(new Person2("a4", 13));

		for (Person2 person2 : hs) {
			System.out.println(person2);
		}
	}
}

class Person2 {
	private String name;
	private int age;

	Person2(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public int hashCode() {
		return name.hashCode() + age * 37;
	}

	public boolean equals(Object obj) {

		if (!(obj instanceof Person2))
			return false;
		Person2 p = (Person2) obj;
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
		return "Person2 [name=" + name + ", age=" + age + ", toString()="
				+ super.toString() + "]";
	}
}
