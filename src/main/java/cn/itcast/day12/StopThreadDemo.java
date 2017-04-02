package cn.itcast.day12;

/*
 stop方法已经过时。
 如何停止线程？
 只有一种，run方法结束。
 开启多线程运行，运行代码通常是循环结构。
 只要控制住循环，就可以让run方法结束，也就是线程结束。

 特殊情况：
 当线程处于了冻结状态。
 就不会读取到标记。那么线程就不会结束。

 当没有指定的方式让冻结的线程恢复到运行状态是，这时需要对冻结进行清除。
 强制让线程恢复到运行状态中来。这样就可以操作标记让线程结束。

 Thread类提供该方法 interrupt();
 */

class StopThread implements Runnable {
	private boolean flag = true;

	public synchronized void run() {
		while (flag) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName()
						+ "...Exception");
			}
			System.out.println(Thread.currentThread().getName() + "....run");
		}
	}

	public void changeFlag() {
		flag = false;
	}
}

class StopThreadDemo {
	public static void main(String[] args) {
		StopThread st = new StopThread();

		Thread t1 = new Thread(st);
		// 设置守护进程，主线程执行完毕，程序结束
		// t1.setDaemon(true);
		t1.start();

		int num = 0;

		while (true) {
			if (num++ == 60) {
				// st.changeFlag();
				// 终端t1线程的wait()方法，并抛出异常
				t1.interrupt();
				break;
			}
			System.out.println(Thread.currentThread().getName() + "......."
					+ num);
		}
		System.out.println("over");
	}
}
