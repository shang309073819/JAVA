package cn.itcast.day17;

/*
 JDK1.5版本出现的新特性。

 方法的可变参数。
 在使用时注意：可变参数一定要定义在参数列表最后面。
 */

class ParamMethodDemo {
	public static void main(String[] args) {
		/*
		 * 可变参数。 其实就是上一种数组参数的简写形式。 不用每一次都手动的建立数组对象。 只要将要操作的元素作为参数传递即可。
		 * 隐式将这些参数封装成了数组。
		 */
		show("haha", 2, 3, 4, 5, 6);
		// show(2,3,4,5,6,4,2,35,9,"heh");
		// show();

	}

	public static void show(String str, int... arr) {
		System.out.println(arr.length);
	}

}
