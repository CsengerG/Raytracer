package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Sphere extends Shape {
    private Vector3D center;
    private double radius;

    public Sphere(Element e){
        super(e);
        System.out.println("Putting a sphere to the scene!");

        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "position":
                    center = new Vector3D(attr.getValue());
                    break;
                case "radius":
                    radius = Double.parseDouble(attr.getValue());
                    break;
            }
        }
    }

    public RayHit intersection(Ray ray) {
        double A = 1.0;
        double B = ray.getStartPoint().subtract(center).dot(ray.getDirection())*2;
        double C = Math.pow(ray.getStartPoint().subtract(center).magnitude(), 2.0) - Math.pow(radius, 2.0);

        if( B*B - 4*A*C < 0.0 ) return new RayHit();
        else {
            double lambda1 = (-B + Math.sqrt( B * B - 4 * A * C) ) / (2.0 * A);
            double lambda2 = (-B - Math.sqrt( B * B - 4 * A * C) ) / (2.0 * A);

            if ( lambda1 <= 0.0 ) lambda1 = Double.MAX_VALUE;
            if ( lambda2 <= 0.0 ) lambda2 = Double.MAX_VALUE;

            if( Double.min(lambda1, lambda2) == Double.MAX_VALUE ) return new RayHit();
            else {
                Vector3D intPoint = ray.getDirection().scale(Double.min(lambda1, lambda2)).add(ray.getStartPoint());
                return new RayHit(intPoint, ray.getStartPoint(), this);
            }

        }
    }

    public Vector3D getSurfaceNormal(Vector3D point) {
        return point.subtract(center).normalize();
    }

    @Override
    public Vector3D getSurfaceColor(Vector3D point) {
        return surfaceColor;
    }
}
