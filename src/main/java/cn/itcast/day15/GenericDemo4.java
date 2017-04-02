package cn.itcast.day15;

import java.util.*;

/*
 ? 通配符。也可以理解为占位符。
 泛型的限定；
 ？ extends E: 可以接收E类型或者E的子类型。上限。
 ？ super E: 可以接收E类型或者E的父类型。下限
 */

class GenericDemo4 {
	public static void main(String[] args) {
		/*
		 * ArrayList<String> al = new ArrayList<String>();
		 * 
		 * al.add("abc1"); al.add("abc2"); al.add("abc3");
		 * 
		 * ArrayList<Integer> al1 = new ArrayList<Integer>(); al1.add(4);
		 * al1.add(7); al1.add(1);
		 * 
		 * printColl(al); printColl(al1);
		 */

		ArrayList<Person> al = new ArrayList<Person>();
		al.add(new Person("abc1"));
		al.add(new Person("abc2"));
		al.add(new Person("abc3"));
		printColl(al);

		ArrayList<Student> al1 = new ArrayList<Student>();
		al1.add(new Student("abc--1"));
		al1.add(new Student("abc--2"));
		al1.add(new Student("abc--3"));
		printColl1(al1);

	}

	public static void printColl1(Collection<? extends Person> al) {
		for (Person person : al) {
			System.out.println(person.getName());
		}
	}

	public static void printColl(ArrayList<?> al) {
		for (Object object : al) {
			// 不知道传过来的是什么 只能打印toString
			System.out.println(object.toString());
		}
	}
}

class Person {
	private String name;

	Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class Student extends Person {
	Student(String name) {
		super(name);
	}

}

class Worker extends Person {
	Worker(String name) {
		super(name);
	}
}
