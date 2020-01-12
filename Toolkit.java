package digits;

public class Toolkit {

	public static double round(double value, int decimals) {
		
	    int scale = (int) Math.pow(10, decimals);
	    return (double) Math.round(value * scale) / scale; 
	    
	}
	
	public static double sigmoid(double x) {
		
		return 1.0 / (1.0 + Math.pow(Math.E, -x));
		
	}
	
}
