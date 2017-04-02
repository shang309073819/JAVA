package cn.itcast.day18;

//字符串转成字节数组
public class test {
	public static void main(String[] args) {
		byte[] byBuffer = new byte[200];
		String strInput = "abcdefg";
		byBuffer = strInput.getBytes();
//		for (byte b : byBuffer) {
//			System.out.println(b);
//		}

		String strInput1 = "张三";
		byBuffer = strInput1.getBytes();
		for (byte b : byBuffer) {
			System.out.print(b);
		}
	}
}
