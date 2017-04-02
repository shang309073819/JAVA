package cn.itcast.day15;

/*
 泛型类定义的泛型，在整个类中有效。如果被方法使用，
 那么泛型类的对象明确要操作的具体类型后，所有要操作的类型就已经固定了。

 为了让不同方法可以操作不同类型，而且类型还不确定。
 那么可以将泛型定义在方法上。

 特殊之处：
 静态方法不可以访问类上定义的泛型。
 如果静态方法操作的应用数据类型不确定，可以将泛型定义在方法上。
 */

class Demo<T> {
	public void show(T t) {
		System.out.println("show:" + t);
	}

	public <Q> void print(Q q) {
		System.out.println("print:" + q);
	}

	public static <W> void method(W t) {
		System.out.println("method:" + t);
	}
}

class GenericDemo2 {
	public static void main(String[] args) {
		Demo<String> d = new Demo<String>();
		d.show("haha");
		// d.show(4);
		d.print(5);
		d.print("hehe");
		Demo.method("hahahahha");
	}
}
