package cn.wulin.brace.utils.test;

import cn.wulin.brace.utils.MathUtil;

/**
 * 计算百钱白鸡问题
 * @author wubo
 *
 */
public class ArithmeticProblemTest {
	
	public static void main(String[] args) {
		for (int y = 0; y <= 100; y++) {
			double z = arithmetic(y);
			if(MathUtil.isInteger(z)){
				int x = getX(y, (int)z);
				if(whetherConditionOk(x)){
					System.out.println("x,y,z:"+x+","+y+","+(int)z);
				}
			}
		}
	}
	
	/**
	 * 条件 : 3y+7z=600,通过 3y+7z=600 的公式可以
	 * @param y
	 * @return z
	 */
	private static double arithmetic(int y){
		return ((double)(600-3*y)/7);
	}
	
	/**
	 * x 是否满足条件
	 * @return
	 */
	private static boolean whetherConditionOk(int x){
		if(x>=0 && x<=100){
			return true;
		}
		return false;
	}
	
	/**
	 * 得到 x
	 * @param y
	 * @param z
	 * @return
	 */
	private static int getX(int y,int z){
		return 100-y-z;
	}
	
}
