package cn.itcast.day14;

/*
 Collection
 |--List:元素是有序的，元素可以重复。因为该集合体系有索引。
 |--ArrayList:底层的数据结构使用的是数组结构。特点：查询速度很快。但是增删稍慢。线程不同步。
 |--LinkedList:底层使用的链表数据结构。特点：增删速度很快，查询稍慢。线程不同步。
 |--Vector:底层是数组数据结构。线程同步。被ArrayList替代了。因为效率低。

 |--Set：元素是无序，元素不可以重复。、

 List：
 特有方法。凡是可以操作角标的方法都是该体系特有的方法。

 增
 add(index,element);
 addAll(index,Collection);

 删
 remove(index);

 改
 set(index,element);
 查
 get(index):
 subList(from,to);
 listIterator();
 int indexOf(obj):获取指定元素的位置。
 ListIterator listIterator();


 List集合特有的迭代器。ListIterator是Iterator的子接口。

 在迭代时，不可以通过集合对象的方法操作集合中的元素。
 因为会发生ConcurrentModificationException异常。

 所以，在迭代器时，只能用迭代器的放过操作元素，可是Iterator方法是有限的，
 只能对元素进行判断，取出，删除的操作，
 如果想要其他的操作如添加，修改等，就需要使用其子接口，ListIterator。

 该接口只能通过List集合的listIterator方法获取。

 */

import java.util.*;

/*
 去除ArrayList集合中的重复元素。
 */

class ArrayListTest {

	public static void sop(Object obj) {
		System.out.println(obj);
	}

	public static void main(String[] args) {
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("java01");
		al.add("java02");
		al.add("java01");
		al.add("java02");
		al.add("java01");
		al.add("java03");

		sop(al);
		al = singleElement(al);
		sop(al);
	}

	public static ArrayList<Object> singleElement(ArrayList<Object> al) {
		// 定义一个临时容器。
		ArrayList<Object> newAl = new ArrayList<Object>();
		Iterator<Object> it = al.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			if (!newAl.contains(obj))
				newAl.add(obj);
		}
		return newAl;
	}
}
