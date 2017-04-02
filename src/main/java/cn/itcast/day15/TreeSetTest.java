package cn.itcast.day15;

/*
 练习：按照字符串长度排序。
 字符串本身具备比较性。但是它的比较方式不是所需要的。
 这时就只能使用比较器。
 */

import java.util.*;

class TreeSetTest {
	public static void main(String[] args) {
		TreeSet<String> ts = new TreeSet<String>(new StrLenComparator());
		ts.add("abcd");
		ts.add("cc");
		ts.add("cba");
		ts.add("aaa");
		ts.add("z");
		ts.add("hahaha");
		for (String string : ts) {
			System.out.println(string);
		}
	}
}

class StrLenComparator implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;

		int num = new Integer(s1.length()).compareTo(new Integer(s2.length()));
		if (num == 0)
			return s1.compareTo(s2);

		return num;
	}
}
