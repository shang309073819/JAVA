package cn.itcast.day17;

/*
 Arrays:用于操作数组的工具类。
 里面都是静态方法。

 asList:将数组变成list集合
 */

import java.util.*;

class ArraysDemo {
	public static void main(String[] args) {
		String[] arr = { "abc", "cc", "kkkk" };

		/*
		 * 把数组变成list集合有什么好处？ 可以使用集合的思想和方法来操作数组中的元素。 注意：将数组变成集合，不可以使用集合的增删方法。
		 * 因为数组的长度是固定。 contains。 get indexOf() subList();
		 * 如果你增删。那么会反生UnsupportedOperationException,
		 */

		List<String> list = Arrays.asList(arr);
		sop("contains:" + list.contains("cc"));
		// list.add("qq");// UnsupportedOperationException,
		sop(list);

		/*
		 * 如果数组中的元素都是对象。那么变成集合时，数组中的元素就直接转成集合中的元素。
		 * 如果数组中的元素都是基本数据类型，那么会将该数组作为集合中的元素存在。
		 */

		int[] nums = { 2, 4, 5 };
		List<int[]> li = Arrays.asList(nums);
		sop(li);

		Integer[] nums1 = { 2, 4, 5 };
		List<Integer> li1 = Arrays.asList(nums1);
		sop(li1);

	}

	public static void sop(Object obj) {
		System.out.println(obj);
	}

}
