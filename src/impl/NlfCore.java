package impl;

import java.util.ArrayList;
import org.apfloat.*;

public class NlfCore {
	
	
	
	
	private static Apfloat n1;
	private static int n = 5;//11.0
	private static int h1SumCap = 64;//450;//768;//256;
	private static double h1SumDensity = 0.5;
	private static Apint h1SumModulus = Apint.ONE;
	private static long k_exponent = 5L;
	public static final Apfloat EXPONENT_MULTIPLE = ApfloatMath.pow(new Apfloat(2),k_exponent);
	private static Apint f_0_mult = ApintMath.pow(new Apint(2), k_exponent);
	private static int h1SumMult = 100;
	private static Apfloat gMult = new Apfloat(0.001);
	private static Apfloat G_mult1 = new Apfloat(10000);
	private static Apfloat G_mult2 = new Apfloat(1000);
	private static Apfloat apk;
	private static int maxIterations = 500;
	private static double defaultIncrement = 0.001;// 0.00001;
	private static double increment; //0.001
	private static double thresholdLower = 0.0005;
	private static double thresholdUpper = 0.001;//0.1
	private static Apfloat gthreshold = Apfloat.ONE;
	private static double criticalLine = 0.8;
	private static boolean debugData = false;
	private static int defaultTries = 1;
	private static int maxTries = defaultTries;	
	//private static double deg = n;

	private static String results;
	private static ArrayList<String> subResults = new ArrayList<>();
	private static int calculations = 0;
	private static long startTime;
	
	public static String start(String s,boolean debug) {
		
		debugData = debug; //get the debug info
		startTime = System.currentTimeMillis();
		results = "";	//reset the result string
		subResults.clear();
		calculations = 0;	//reset the calculation counter
		maxTries = defaultTries; //reset max. tries in the search segment
		increment = defaultIncrement; //reset increment
		
		subResults.add(s);
		ListIterator(subResults);

		if(debugData) {
			results += "\ncalculations: "+calculations;
			results += "\ntime elapsed: "+(System.currentTimeMillis()-startTime);
		}
		
		
		return results.substring(0, results.length()-3);
	} 
	
	private static void ListIterator(ArrayList<String> l) {
		int i = 0;
		String t = "";

		while(!l.isEmpty()) {
			t = l.get(i);
			l.remove(i);
			System.out.println(l.toString());
			System.out.println("results: "+results);
			
			if(new Apfloat(t).compareTo(Apfloat.ONE) != 0) {
				factorize(t,debugData);
			}
			
		}
	}
	
	public static void factorize(String s,boolean debug) {
		int limitOfOperations;
		int index;
		double initialPoint;
		Apfloat a = new Apfloat(0,12);
		Apint ap210 = new Apint(210);
		Apint ap2 = new Apint(2);
		maxTries = defaultTries;
		
		n1 = new Apfloat(s,12);
		a = ApfloatMath.sqrt(n1);

		/* 
		if(Math.pow(a, 1.0/n) < 1.5){
			dir = true;
			increment = defaultIncrement;
			a = list(Math.sqrt(n1),dir);
		}
		if(a == 0){
			return "factorization of "+ s +" failed!";
		}
		
		*/
		/*
		for(double i = 10.0; i<1.0; i--) {
			increment = defaultIncrement;
			a = polynomialSieve(a, false, i);
			System.out.println("x ^ n = "+a);
		}
		*/
		/*
		while(maxTries > 0) {
			//setup the trial-and-error segment:
			if(ApfloatMath.pow(a,n).compareTo(new Apint(5)) == -1) {
				//return the probable prime
				System.out.println("tänne");
				System.out.println("this should return"+n1);
				return n1.toString(true);
			}		
			a = a.ceil();

		}
		*/
		
		//a = polynomialSieve(a, false, n);

		
		int i = 0;
		int g_increment = 25;
		
		Apfloat dist = new Apfloat(0,16); 
		dist = NlfFineDataManager.calculateDistances(apk, n, a, G_mult1);
		System.out.println("dist"+dist.toString(true));
		
		maxIterations = 15;
		
		while (maxIterations > 0) {
			System.out.println("G: "+G_function(i,a,dist,true).toString(true));
			
			while (G_function(i,a,dist,true).compareTo(gthreshold) == -1) {
				i += g_increment;
				
			}
			g_increment = g_increment / 5;
			while (G_function(i,a,dist,true).compareTo(gthreshold) == 1) {
				//System.out.println(G_function(i,a,dist,true).toString(true));
				//System.out.println(i);
				i -= g_increment;
			}
			g_increment = g_increment / 5;
			maxIterations--;
		}
		a = G_function(i,a,dist,false);
		
		maxIterations = 500;
		
		
		
		NlfFineDataManager.init();
		
		while(maxTries > 0) {
			//setup the trial-and-error segment:
			if(ApfloatMath.pow(a,n).compareTo(new Apint(5)) == -1) {
				//return the probable prime
				System.out.println("tänne");
				System.out.println("this should return"+n1);
				results += n1.toString(true)+" | ";
				return;
			}
			
			a = a.ceil();
			limitOfOperations = 420;
			index = (ap2.multiply(ApfloatMath.abs(a)).mod(ap210)).intValue();
			System.out.println("integer close to result:"+a);
			a = a.add(ap210).subtract(a.mod(ap210)).add(ap2.multiply(a).mod(ap210));
			//a = a + 210 - (a % 210) + (2*a % 210); 
			
			System.out.println("iteration start:"+a.toString(true));
			System.out.println("index:"+index);
			
			while(limitOfOperations > 0){
				if(debugData) {
					calculations++;
				}
				System.out.println(NlfFineDataManager.ModulusList[index]);
				if(NlfFineDataManager.ModulusList[index] != 0 && a != n1) {
					System.out.println("//"+a.toString(true)+" "+n1.mod(a));

					
					if(n1.mod(a).compareTo(Apfloat.ZERO) == 0 && !(a.compareTo(Apfloat.ONE) == 0 || a.compareTo(Apfloat.ONE.negate()) == 0) && a.compareTo(n1) != 0){
						increment = defaultIncrement;
						System.out.println("factor:"+a);
						subResults.add(a.toString(true));
						subResults.add(n1.divide(a).toString(true));

						return;
					}
				}
				
				limitOfOperations--;
				
				index = Math.floorMod((index - 1),210);
				a = a.subtract(Apfloat.ONE);
				a = ApfloatMath.abs(a);
				

				
			}
			
			maxTries--;
			//a = list(initialPoint,false,n);
			
		}
		
		//we have a probable prime
		results += n1.toString(true)+" | ";
		return;

	}
	
	
	/*
	 * Function for finding the "rightmost" real x in local region at graph of sum h_1(x) 
	 * for which h_1(x) > thresholdUpper + h_1(x +epsilon) for some epsilon > 0
	 * 
	 * e.g for ten values of A(x): (0-9) 0.1 - 0.1 - 0.1 - 1.5 - 1.67 - 2.11 - 1.82 - 1.35 - 0.1 - 0.1
	 * here 7, which gives 1.35, is the "rightmost" value, giving the best approximation
	 * Approximation can still be more or less wrong, depending on precision and 
	 * the "size of the epsilon"; the closer to the edge, the better.
	 * Best value is e.g. x, such that, when A(x) > thresholdUpper, adding marginal amount like
	 * 10^-6 to the x immediately causes A(x) to fall way under both thresholds
	 */
	
	protected static Apfloat polynomialSieve(Apfloat point,boolean up,int degree) {
		double initialPoint = 0;
		double bx = 0;
		boolean notARealPoint = true;
	
		if(!up){
			increment *= -1.0;
		}
	
		initialPoint = ApfloatMath.pow(point, new Apfloat(1.0,32).divide(new Apfloat(degree))).doubleValue();
		System.out.println(degree+"th root: "+initialPoint);
		
		double highestPoint;
		double x = 0;
		x = initialPoint;
		double y = h_1(x,degree);
		int maxPruningOp = (int) (Math.abs(initialPoint)*Math.pow(increment,-1));
		double startingPoint = 0;
		
		
		
		while(notARealPoint){
			x  += increment;
			y = h_1(x,degree);
			
			
			if(x > 62.57 && x < 62.61) {
				System.out.println("x:"+x+" y:"+y+" x^n:"+ApfloatMath.pow(new Apfloat(x), degree)+" inc:"+increment);
			}
			
			if(debugData) {
				System.out.println("x:"+x+" y:"+y+" x^n:"+ApfloatMath.pow(new Apfloat(x), degree)+" inc:"+increment);
			}
			
			
			maxPruningOp--;
			if(maxPruningOp == 1){
				return new Apfloat(0);
			}
			
			//Additional tests for the point
			if(y>thresholdUpper) {
				for(int i = 0; i < 64; i++) {
					bx = x;
					bx += increment * 0.00001;
					if(h_1(bx,degree) > criticalLine) {
						notARealPoint = false;
					}
				}
				if(debugData) {
					calculations++;
				}
			}
	   	}
		
		notARealPoint = true;
		
		if(up){
			startingPoint = x + increment;
			while(y > thresholdUpper){
				startingPoint += increment;
				y = h_1(startingPoint,degree);
			}
		}
		else{
			startingPoint = x-increment;
		}
	
		highestPoint = y;
		while(maxIterations > 0){
			x = startingPoint+increment;
			y = h_1(x,degree);
			
			if(y < thresholdLower){
				startingPoint = x;
			}
			else {
				increment /= 3;
			}
	
			if(y > highestPoint){
				highestPoint = y;
			}
			
			maxIterations--;
			
			if(debugData) {
				System.out.println("st. point :"+startingPoint);
				System.out.println("x:"+x);
				System.out.println("inc:"+increment);
				System.out.println("y: "+y);
			}
			
		}
		increment = defaultIncrement;
		
		System.out.println("edge point: "+x+"value h_1(x): "+y);

		/*
		if(Math.round(Math.pow(x, deg)) % 2 == 1) {
			a = h_1(Math.round(Math.pow(x,deg)),deg,8.0,1000);
		}
		else {
			a = h_1(Math.round(Math.pow(x,deg))-1,deg,8.0,1000);
		}*/


		
		return ApfloatMath.pow(new Apfloat(x),degree);
	
		
		
	}

	
	private static Apfloat f_0(Apfloat x){
		if(debugData) {
			calculations++;
		}
		return n1.add(ApfloatMath.floor(x).divide(f_0_mult)).mod(n1.add(n1.mod(ApfloatMath.floor(x).divide(f_0_mult)))).multiply(f_0_mult);
		
		//(n1 + Math.floor(x)) % (n1 + n1 % Math.floor(x));
	}
	
	private static Apfloat g(double x){
		Apfloat lx = new Apfloat(x,32);
		Apfloat n1_d = f_0(lx);
		Apfloat divisor = new Apfloat(1,8);
		
		if(lx.subtract(n1_d) == Apfloat.ZERO) {
			divisor = new Apfloat(0.000000000001,8);
		}
		else {
			/*System.out.println((lx).toString(true));
			System.out.println(lx.precision());
			System.out.println(lx.multiply(lx).precision());
			System.out.println(ApfloatMath.sin(lx).precision());
			System.out.println((ApfloatMath.sin(lx)).toString(true)+" - "+(new Apfloat(0.5)).toString(true));*/
			divisor = lx.subtract(n1_d).subtract(ApfloatMath.sin(lx.multiply(ApfloatMath.sqrt(lx)))).subtract(new Apfloat(0.5));
		}
		
		try {
			gMult.divide(divisor);
		}
		catch(ArithmeticException e) {
			return new Apfloat(0.99);
		}
		
		return gMult.divide(divisor);
	}
	
	private static double h_1(double x, int deg){
		double sum = 0;

		for(int i = 1; i < h1SumCap; i++){
			//System.out.println(i);
			//System.out.println("h_1,i : "+i+"sum : "+sum+" x : "+x);
			sum = sum + (g(Math.pow(x,deg)+(h1SumDensity*i)).mod(h1SumModulus)).doubleValue();
		}

		return h1SumMult*sum;
	}
	
	private static Apfloat G_function(int x, Apfloat a, Apfloat dist, boolean compute) {
		apk = new Apfloat(k_exponent);
		
		
		Apfloat temp = NlfFineDataManager.returnLargerPoint().subtract(new Apfloat(x/10)); //System.out.println("t1:"+temp.toString(true));
		Apfloat temp2 = temp.multiply(dist); //System.out.println("t2:"+temp2);
		
		if(compute) {			
			return G_mult2.multiply(new Apfloat(h_1(temp2.divide(G_mult1).doubleValue(),n)));
		}
		else {
			return ApfloatMath.pow(temp2.divide(G_mult1), new Apfloat(n)).divide(EXPONENT_MULTIPLE);
			
		}


	}
	
	/*
	private static double A(double x){		
		double result = h_1(x)-h_1(x + AEpsilon);
		return Math.abs(result);
	}
	*/

	
}

