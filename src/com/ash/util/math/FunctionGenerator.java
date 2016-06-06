package com.ash.util.math;

/**
 * Generates formatted functions of various degrees
 * @author Aaron Hilbig
 *
 */
public class FunctionGenerator {
	
	public static final String PARAM_CUBIC_FUNCTION = "x³,x²,x, ";
	public static final String PARAM_THIRD_DEGREE_FUNCTION = "x³,x²,x, ";
	public static final String PARAM_SECOND_DEGREE_FUNCTION = "x²,x, ";
	public static final String PARAM_FUNCTION = "x, ";
	public static final String DEFAULT_PREFIX = "f(x) = ";
	
	public String prefix = DEFAULT_PREFIX;
	
	public String function;
	
	public FunctionGenerator(String prefix, String params, double... x){
		this.prefix = prefix;
		function = generateWithPrefix(params, x);
	}
	
	private String generateWithPrefix(String params, double... x){
		String[] param = params.split(",");
		String ret = prefix;
		int index = 0;
		for(double d : x){
			String ins = d==1 ? "" : "" + Math.abs(d);
			if(d > 0 && index!=0){
				ret += " + " + ins + param[index].trim();
			} else if(d > 0){
				ret += ins + param[index];
			} else if(d < 0 && index!=0){
				ret += " - " +  ins + param[index].trim();
			} else if(d < 0){
				ret += "- " +  ins + param[index].trim();
			}
			index++;
		}
		return ret;
	}
	
	
	public static String generate(String params, double... x){
		String[] param = params.split(",");
		String ret = DEFAULT_PREFIX;
		int index = 0;
		for(double d : x){
			if(d > 0 && index!=0){
				ret += " + " + d + param[index].trim();
			} else if(d > 0){
				ret += d + param[index];
			} else if(d < 0 && index!=0){
				ret += " - " +  Math.abs(d) + param[index].trim();
			} else if(d < 0){
				ret += "- " +  Math.abs(d) + param[index].trim();
			}
			index++;
		}
		return ret;
	}
	
}
