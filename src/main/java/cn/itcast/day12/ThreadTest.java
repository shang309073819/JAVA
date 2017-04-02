package cn.itcast.day12;

//开发中常用的线程写法

class ThreadTest {
	public static void main(String[] args) {

		new Thread() {
			public void run() {
				for (int x = 0; x < 100; x++) {
					System.out.println(Thread.currentThread().getName()
							+ "....." + x);
				}
			}
		}.start();

		for (int x = 0; x < 100; x++) {
			System.out.println(Thread.currentThread().getName() + "....." + x);
		}

		Runnable r = new Runnable() {
			public void run() {
				for (int x = 0; x < 100; x++) {
					System.out.println(Thread.currentThread().getName()
							+ "....." + x);
				}
			}
		};
		new Thread(r).start();
	}
}
