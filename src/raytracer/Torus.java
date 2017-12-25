package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Torus extends Shape {
    private double r;
    private double R;
    private Vector3D position;
    private Vector3D orientation;

    public Torus(Element e){
        super(e);
        System.out.println("Putting a torus to the scene");

        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "position":
                    position = new Vector3D(attr.getValue());
                    break;
                case "orientation":
                    orientation = (new Vector3D(attr.getValue())).normalize();
                    break;
                case "r1":
                    R = Double.parseDouble(attr.getValue());
                    break;
                case "r2":
                    r = Double.parseDouble(attr.getValue());
                    break;
            }
        }
    }

    public RayHit intersection(Ray ray) {
        // need orientation to be (0,0,1) for calculation
        double zAngle;
        double yAngle = Math.acos(orientation.z);

        if( orientation.x == 0 && orientation.y == 0 ) zAngle = 0.0;
        else zAngle = Math.acos(orientation.x / Math.sqrt(Math.pow(orientation.x,2.0) + Math.pow(orientation.y,2.0)));

        // transform ray
        Vector3D rayStart = ray.getStartPoint().subtract(position).rotateZ(zAngle).rotateY(yAngle);
        Vector3D rayDir = ray.getDirection().rotateZ(zAngle).rotateY(yAngle);

//        double xe = ray.getStartPoint().x; double xe2 = xe*xe; double xe3 = xe2*xe; double xe4 = xe2*xe2;
//        double ye = ray.getStartPoint().y; double ye2 = ye*ye; double ye3 = ye2*ye; double ye4 = ye2*ye2;
//        double ze = ray.getStartPoint().z; double ze2 = ze*ze; double ze3 = ze2*ze; double ze4 = ze2*ze2;

        // use transformed ray
        double xe = rayStart.x; double xe2 = xe*xe; double xe3 = xe2*xe; double xe4 = xe2*xe2;
        double ye = rayStart.y; double ye2 = ye*ye; double ye3 = ye2*ye; double ye4 = ye2*ye2;
        double ze = rayStart.z; double ze2 = ze*ze; double ze3 = ze2*ze; double ze4 = ze2*ze2;

        double R2 = R*R; double R4 = R2*R2;
        double r2 = r*r; double r4 = r2*r2;

//        double xd = ray.getDirection().x; double xd2 = xd*xd; double xd3 = xd2*xd; double xd4 = xd2*xd2;
//        double yd = ray.getDirection().y; double yd2 = yd*yd; double yd3 = yd2*yd; double yd4 = yd2*yd2;
//        double zd = ray.getDirection().z; double zd2 = zd*zd; double zd3 = zd2*zd; double zd4 = zd2*zd2;

        // use transformed ray
        double xd = rayDir.x; double xd2 = xd*xd; double xd3 = xd2*xd; double xd4 = xd2*xd2;
        double yd = rayDir.y; double yd2 = yd*yd; double yd3 = yd2*yd; double yd4 = yd2*yd2;
        double zd = rayDir.z; double zd2 = zd*zd; double zd3 = zd2*zd; double zd4 = zd2*zd2;

        double A = xd4 + yd4 + zd4 + 2*xd2*yd2 + 2*xd2*zd2 + 2*yd2*zd2;
        double B = 4*xd3*xe+4*yd3*ye+4*zd3*ze+4*xd2*yd*ye+4*xd2*zd*ze+4*xd*xe*yd2+4*yd2*zd*ze+4*xd*xe*zd2+4*yd*ye*zd2;
        double C = -2*R2*xd2 - 2*R2*yd2 + 2*R2*zd2-2*r2*xd2-2*r2*yd2-2*r2*zd2+6*xd2*xe2+2*xe2*yd2+8*xd*xe*yd*ye+2*xd2*ye2+6*yd2*ye2+2*xe2*zd2+2*ye2*zd2+8*xd*xe*zd*ze+8*yd*ye*zd*ze+2*xd2*ze2+2*yd2*ze2+6*zd2*ze2;
        double D = -4*R2*xd*xe-4*R2*yd*ye+4*R2*zd*ze-4*r2*xd*xe-4*r2*yd*ye-4*r2*zd*ze+4*xd*xe3+4*xe2*yd*ye+4*xd*xe*ye2+4*yd*ye3+4*xe2*zd*ze+4*ye2*zd*ze+4*xd*xe*ze2+4*yd*ye*ze2+4*zd*ze3;
        double E = R4-2*R2*xe2-2*R2*ye2+2*R2*ze2+r4-2*r2*R2-2*r2*xe2-2*r2*ye2-2*r2*ze2+xe4+ye4+ze4+2*xe2*ye2+2*xe2*ze2+2*ye2*ze2;

        Complex a = new Complex(A);
        Complex b = new Complex(B);

        Complex p = new Complex((8*A*C - 3*B*B) / (8*A*A));
        Complex q = new Complex((B*B*B - 4*A*B*C + 8*A*A*D) / (8*A*A*A));

        Complex delta0 = new Complex(C*C - 3*B*D + 12*A*E);
        Complex delta1 = new Complex(2*C*C*C - 9*B*C*D + 27*B*B*E + 27*A*D*D - 72*A*C*E);

        Complex Q = (delta1.pow(2).subtract( delta0.pow(3).scale(4.0) )).sqrt().add(delta1).scale(0.5).cbrt();
        Complex S = delta0.div(Q).add(Q).div(a.scale(3.0)).add( p.scale(-2.0/3.0) ).sqrt().scale(0.5);

        Complex lambda1 = S.pow(2.0).scale(-4.0).subtract( p.scale(2.0)).add( q.div(S) ).sqrt().scale(0.5).add( b.scale(-1.0).div( a.scale(4.0) ) ).add(S.scale(-1.0));
        Complex lambda2 = S.pow(2.0).scale(-4.0).subtract( p.scale(2.0)).add( q.div(S) ).sqrt().scale(-0.5).add( b.scale(-1.0).div( a.scale(4.0) ) ).add(S.scale(-1.0));

        Complex lambda3 = S.pow(2.0).scale(-4.0).subtract( p.scale(2.0)).subtract( q.div(S) ).sqrt().scale(0.5).add( b.scale(-1.0).div( a.scale(4.0) ) ).add(S);
        Complex lambda4 = S.pow(2.0).scale(-4.0).subtract( p.scale(2.0)).subtract( q.div(S) ).sqrt().scale(-0.5).add( b.scale(-1.0).div( a.scale(4.0) ) ).add(S);

        double l1 = -1.0;
        double l2 = -1.0;
        double l3 = -1.0;
        double l4 = -1.0;

        if( lambda1.isReal() ) l1 = lambda1.realPart();
        if( lambda2.isReal() ) l2 = lambda2.realPart();
        if( lambda3.isReal() ) l3 = lambda3.realPart();
        if( lambda4.isReal() ) l4 = lambda4.realPart();

        if( l1 < 0.0 ) l1 = Double.MAX_VALUE;
        if( l2 < 0.0 ) l2 = Double.MAX_VALUE;
        if( l3 < 0.0 ) l3 = Double.MAX_VALUE;
        if( l4 < 0.0 ) l4 = Double.MAX_VALUE;

        if( Double.min(Double.min(l1, l2), Double.min(l3, l4)) == Double.MAX_VALUE ) {
            return new RayHit();
        } else {
            double lambda = Double.min(Double.min(l1, l2), Double.min(l3, l4));
            //Vector3D intPoint = ray.getDirection().scale(lambda).add(ray.getStartPoint());

            // intersection in this coordinate system:
            Vector3D thisInt = rayDir.scale(lambda).add(rayStart);

            // transform this back
            Vector3D intPoint = thisInt.rotateY(-yAngle).rotateZ(-zAngle).add(position);

            return new RayHit(intPoint, ray.getStartPoint(), this);
        }
    }

    public Vector3D getSurfaceNormal(Vector3D point) {
        // transform point
        // need orientation to be (0,0,1) for calculation
        double zAngle;
        double yAngle = Math.acos(orientation.z);

        if( orientation.x == 0 && orientation.y == 0 ) zAngle = 0.0;
        else zAngle = Math.acos(orientation.x / Math.sqrt(Math.pow(orientation.x,2.0) + Math.pow(orientation.y,2.0)));

        Vector3D tPoint = point.subtract(position).rotateZ(zAngle).rotateY(yAngle);

        Vector3D n = new Vector3D(0.0, 0.0, tPoint.z);
        Vector3D inPlane = tPoint.subtract(n);
        Vector3D onCircle = inPlane.normalize().scale(R);

        // transform back
        Vector3D normal = tPoint.subtract(onCircle).normalize();
        Vector3D N = normal.rotateY(-yAngle).rotateZ(-zAngle);

        return N;
    }

    private double poly(double a, double b, double c, double d, double e, double x){
        return a*Math.pow(x,4) + b*Math.pow(x,3) + c*Math.pow(x,2) + d*x + e;
    }

    @Override
    public Vector3D getSurfaceColor(Vector3D point) {
        return surfaceColor;
    }
}
