package raytracer;

public class Complex {
    private double theta;
    private double r;
    private double Re;
    private double Im;

    public Complex(double re){
        theta = 0.0;
        Im = 0.0;
        r = re;
        Re = re;
    }

    public Complex(double re, double im){
        Re = re;
        Im = im;

        theta = Math.atan2(im,re);
        r  = Math.sqrt(re*re+im*im);
    }

    public Complex(double magn, double phi, String s){
        theta = phi;
        r = magn;

        Re = magn*Math.cos(theta);
        Im = magn*Math.sin(theta);
    }

    public Complex add(Complex c){
        return new Complex(Re + c.realPart(), Im + c.imPart());
    }

    public Complex subtract(Complex c){
        return new Complex(Re - c.realPart(), Im - c.imPart());
    }

    public Complex scale(double k){
        return new Complex(Re*k, Im*k);
    }

    public Complex scale(Complex c){
        return new Complex(r*c.magnitude(), c.arg() + theta, "polar");
    }

    public Complex div(Complex c){
        return new Complex(r/c.magnitude(), theta - c.arg(), "polar");
    }

    public Complex sqrt(){
        return new Complex(Math.sqrt(r), theta/2.0, "polar");
    }

    public Complex cbrt(){
        return new Complex(Math.cbrt(r), theta/3.0, "polar");
    }

    public Complex pow(double x){
        return new Complex(Math.pow(r,x), theta*x, "polar");
    }

    public double realPart(){return Re;}
    public double imPart(){return Im;}
    public double arg(){return theta;}
    public double magnitude(){return r;}
    public boolean isReal(){return (Math.abs(Im) < (1e-4));}
    public String show (){
        return "Complex: Re = " + Re + " Im = " + Im + " magn = " + r + " theta = " + theta;
    }
}
