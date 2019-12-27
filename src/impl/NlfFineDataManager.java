package impl;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.apfloat.ApintMath;

public class NlfFineDataManager {
	
	public static int[] ModulusList = new int[210];
	private static Apfloat y2;
	private static Apfloat x = new Apfloat(0,15);
	private static Apfloat two = new Apfloat(2);
	
	public static void init() {
		int x = 210;
		
		for(int i = 0; i<210; i++) {
			ModulusList[i] = ((x) % 2)*((x) % 3)*((x) % 5)*((x) % 7); 
			x++;
		}
	}
	
	
	private static Apfloat exponentMultipleFunction(Apfloat x, Apfloat k, long n, Apfloat mult) {
		Apfloat fxmult = new Apfloat(500);
		
		return fxmult.multiply(ApfloatMath.fmod(ApfloatMath.floor(ApfloatMath.pow(x.divide(mult),n)).divide(NlfCore.EXPONENT_MULTIPLE),Apfloat.ONE));

	}
	
	public static Apfloat calculateDistances(Apfloat k, long n, Apfloat sqrt_n,Apfloat mult) {
		x = ApfloatMath.root(NlfCore.EXPONENT_MULTIPLE.multiply(sqrt_n), n).multiply(mult);
		
		Apfloat y1 = Apfloat.ZERO;
		y2 = Apfloat.ZERO;
		
		for(int i = 2; i!=0; i--) {
			while(exponentMultipleFunction(x,k,n,mult).compareTo(Apfloat.ZERO) == 1) {
				System.out.println("while loop: i = "+i);
				x = x.subtract(Apfloat.ONE);
				System.out.println(x.toString(true));
				System.out.println(exponentMultipleFunction(x,k,n,mult).toString(true));
			}
			
			if(i==1) {
				y1 = x;
				System.out.println("y1: "+y1.toString(true));
				//System.out.println("y2: "+y2.toString(true));
				return y2.subtract(y1);
			}
			
			y2 = x;
			System.out.println("y2: "+y2.toString(true));
			x = x.subtract(new Apfloat(20,15));
			System.out.println(exponentMultipleFunction(x,k,n,mult).toString(true));
		}
		
		return Apfloat.ZERO;
	}
	
	public static Apfloat returnLargerPoint() {
		return y2;
	}
	
	
	
	private static void test(double a) {
		double interval = 2.0;
		double x = 2.0;
		for(double d = 2.0; d < 13; d += 0.00001) {
			//System.out.println(d);
			if((0.1*Math.abs(((a/d)%1)-1) < 0.0001 || 0.1*Math.abs(((a/d)%1)-0) < 0.0001 ) && d-x > 0.005) {
				System.out.println(d-x);
				interval = d-x;
				x = d;
			}
			
		}
	}
	
	

	
		
}
