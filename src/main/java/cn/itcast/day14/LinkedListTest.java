package cn.itcast.day14;

/*
 LinkedList:特有方法：
 addFirst();
 addLast();

 getFirst();
 getLast();
 获取元素，但不删除元素。如果集合中没有元素，会出现NoSuchElementException

 removeFirst();
 removeLast();
 获取元素，但是元素被删除。如果集合中没有元素，会出现NoSuchElementException


 在JDK1.6出现了替代方法。

 offerFirst();
 offerLast();


 peekFirst();
 peekLast();
 获取元素，但不删除元素。如果集合中没有元素，会返回null。

 pollFirst();
 pollLast();
 获取元素，但是元素被删除。如果集合中没有元素，会返回null。

 */

/*
 使用LinkedList模拟一个堆栈或者队列数据结构。
 堆栈：先进后出  如同一个杯子。
 队列：先进先出 First in First out  FIFO 如同一个水管。
 */

import java.util.*;

class DuiLie {
	private LinkedList<Object> link;

	DuiLie() {
		link = new LinkedList<Object>();
	}

	public void myAdd(Object obj) {
		link.addFirst(obj);
	}

	public Object myGet() {
		return link.removeFirst();
	}

	public boolean isNull() {
		return link.isEmpty();
	}

}

class LinkedListTest {
	public static void main(String[] args) {
		DuiLie dl = new DuiLie();
		dl.myAdd("java01");
		dl.myAdd("java02");
		dl.myAdd("java03");
		dl.myAdd("java04");

		while (!dl.isNull()) {
			System.out.println(dl.myGet());
		}
	}
}
