package model;

import java.text.DecimalFormat;

public class Calc {

	final static DecimalFormat df = new DecimalFormat("###.##");
	
	public static double grossCalc7i(double rate, double reg, double ot) {
		return Double.valueOf(df.format(rate * (reg + ot/2)));
	}
	
	public static double grossCalc(double rate, double reg, double ot) {
		return Double.valueOf(df.format(rate * reg));
	}
	
	public static double grossCalcOT(double rate, double reg, double ot) {
		return Double.valueOf(df.format(rate * reg + rate / 2 * ot));
	}
	
	public static double rateCalc7i(double gross, double reg, double ot) {
		return Double.valueOf(df.format(gross / (reg + ot / 2)));
	}
	
	public static double rateCalc(double gross, double reg) {
		return Double.valueOf(df.format(gross / reg));
	}
	
	public static Object testObj(Object o) throws NullPointerException {
		Object t;
		try {
			t = o;
		} catch(Exception e) {
			t = null;
		}
		
		return t;
	}
}
