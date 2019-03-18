package cn.wulin.brace.utils;

/**
 * Math工具类
 * @author wubo
 *
 */
public class MathUtil {
	
	/** 
	 * 判断double是否是整数 
	 * @param obj 
	 * @return 
	 */  
	public static boolean isInteger(double obj) {  
	    double eps = 1e-10;  // 精度范围  
	    return obj-Math.floor(obj) < eps;  
	} 

}
