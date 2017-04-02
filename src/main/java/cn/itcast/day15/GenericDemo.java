package cn.itcast.day15;

/*
 泛型：JDK1.5版本以后出现新特性。用于解决安全问题，是一个类型安全机制。

 好处
 1.将运行时期出现问题ClassCastException，转移到了编译时期。，
 方便于程序员解决问题。让运行时问题减少，安全。，

 2，避免了强制转换麻烦。

 泛型格式：通过<>来定义要操作的引用数据类型。

 在使用java提供的对象时，什么时候写泛型呢？

 通常在集合框架中很常见，
 只要见到<>就要定义泛型。

 其实<> 就是用来接收类型的。

 当使用集合时，将集合中要存储的数据类型作为参数传递到<>中即可。

 */

/*
 * 
 泛型类。
 什么时候定义泛型类？
 当类中要操作的引用数据类型不确定的时候，
 早期定义Object来完成扩展。
 现在定义泛型来完成扩展。

 */
class Utils<QQ> {
	private QQ q;

	public void setObject(QQ q) {
		this.q = q;
	}

	public QQ getObject() {
		return q;
	}
}

class GenericDemo {
	public static void main(String[] args) {

//		Utils<Worker> u = new Utils<Worker>();
//		Worker w = u.getObject();
//		// u.setObject(new Student());
	}
}
