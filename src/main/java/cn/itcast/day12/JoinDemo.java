package cn.itcast.day12;

/*
 join:
 当A线程执行到了B线程的.join()方法时，A就会等待。等B线程都执行完，A才会执行。
 join可以用来临时加入线程执行。
 */

class Demo implements Runnable {
	public void run() {
		for (int x = 0; x < 70; x++) {
			System.out.println(Thread.currentThread().toString() + "....." + x);
			// 暂停当前正在执行的线程，并执行其他线程，减少该线程的执行频率。临时释放执行权，能达到各个线程平均执行。
			// Thread.yield();
		}
	}
}

class JoinDemo {
	public static void main(String[] args) throws Exception {
		Demo d = new Demo();
		Thread t1 = new Thread(d);
		Thread t2 = new Thread(d);
		t1.start();
		// 优先级越高，执行的频率越高
		t1.setPriority(Thread.MAX_PRIORITY);
		t2.start();

		// 主线程释放执行权，等到t1执行完，主线程才开始执行
		t1.join();

		for (int x = 0; x < 80; x++) {
			System.out.println("main....." + x);
		}
		System.out.println("over");
	}
}
