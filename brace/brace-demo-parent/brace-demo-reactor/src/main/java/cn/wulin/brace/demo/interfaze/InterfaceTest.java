package cn.wulin.brace.demo.interfaze;

public class InterfaceTest {
	
	public static interface A {
		default void test1() {
			System.out.println("A.test1");
		}
		
	}
	
	public static interface B {
		default void test1() {
			System.out.println("B.test1");
		}
		
		default void xx() {
			System.out.println("B.xx");
		}
	}
	
	public static class AB implements A,B{

		@Override
		public void test1() {
			// TODO Auto-generated method stub
			B.super.test1();
		}

//		@Override
//		public void test1() {
//			A.super.test1();
//		}
//		
	}
	
	public static void main(String[] args) {
		AB ab = new AB();
		ab.test1();
		ab.xx();
	}

}
