package FFT;

public class Complex {

	double a,b;
	
	public Complex(double a, double b){
		this.a = a;
		this.b = b;
	}
	
	public Complex times(Complex z){
		return new Complex(a*z.a-b*z.b,a*z.b+b*z.a);
	}
	
	public Complex times(double x){
		return new Complex(a*x,b*x);
	}
	
	public Complex conjugate(){
		return new Complex(a,-b);
	}
	
	public Complex plus(Complex z){
		return new Complex(z.a+a,z.b+b);
	}
	
	public Complex minus(Complex z){
		return new Complex(a-z.a,b-z.b);
	}
}
