package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Cylinder extends Shape {
    private double R;
    private Vector3D position;
    private Vector3D orientation;
    private double height;

    public Cylinder(Element e){
        super(e);

        System.out.println("Putting a cylinder to the scene");

        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "position":
                    position = new Vector3D(attr.getValue());
                    break;
                case "radius":
                    R = Double.parseDouble(attr.getValue());
                    break;
                case "orientation":
                    orientation = (new Vector3D(attr.getValue())).normalize();
                    break;
                case "height":
                    height = Double.parseDouble(attr.getValue());
                    break;
            }
        }
    }

    @Override
    public RayHit intersection(Ray ray) {
        // need orientation to be (0,0,1) for calculation
        double zAngle;
        double yAngle = Math.acos(orientation.z);

        if( orientation.x == 0 && orientation.y == 0 ) zAngle = 0.0;
        else zAngle = Math.acos(orientation.x / Math.sqrt(Math.pow(orientation.x,2.0) + Math.pow(orientation.y,2.0)));

        // transform ray
        Vector3D rayStart = ray.getStartPoint().subtract(position).rotateZ(zAngle).rotateY(yAngle);
        Vector3D rayDir = ray.getDirection().rotateZ(zAngle).rotateY(yAngle);

        double u1 = rayDir.x;
        double u2 = rayDir.y;
        double a1 = rayStart.x;
        double a2 = rayStart.y;

        double A = u1*u1 + u2*u2;
        double B = 2*(a1*u1 + a2*u2);
        double C = a1*a1 + a2*a2 - R*R;

        double D = B*B - 4*A*C;

        if(D < 0.0) return new RayHit();
        else {
            double t1 = (-B + Math.sqrt(D)) / (2*A);
            double t2 = (-B - Math.sqrt(D)) / (2*A);

            if( Math.abs( rayDir.scale(t1).add(rayStart).z ) > height/2.0 ) t1 = Double.MAX_VALUE;
            if( Math.abs( rayDir.scale(t2).add(rayStart).z ) > height/2.0 ) t2 = Double.MAX_VALUE;

            if( t1 < 0.0 ) t1 = Double.MAX_VALUE;
            if( t2 < 0.0 ) t2 = Double.MAX_VALUE;

            // determine if there is an intersection with top or bottom
            Vector3D n = new Vector3D(0.0, 0.0, 1.0);
            double d,r;

            // top
            d = height / 2.0;

            double t3 = (d - rayStart.dot(n)) / rayDir.dot(n);
            Vector3D topInt = rayDir.scale(t3).add(rayStart);
            r = Math.sqrt( Math.pow(topInt.x, 2.0) + Math.pow(topInt.y, 2.0) );

            if( t3 < 0.0 || R < r) t3 = Double.MAX_VALUE;

            // bottom
            d = -height / 2.0;

            double t4 = (d - rayStart.dot(n)) / rayDir.dot(n);
            Vector3D botInt = rayDir.scale(t4).add(rayStart);
            r = Math.sqrt( Math.pow(botInt.x, 2.0) + Math.pow(botInt.y, 2.0) );

            if( t4 < 0.0 || R < r) t4 = Double.MAX_VALUE;

            double t = Double.min(Double.min(t1, t2),Double.min(t3,t4));

            if( t == Double.MAX_VALUE ) return new RayHit();
            else {
                // intersection in this coordinate system:
                Vector3D thisInt = rayDir.scale(t).add(rayStart);

                // transform this back
                Vector3D intPoint = thisInt.rotateY(-yAngle).rotateZ(-zAngle).add(position);

                return new RayHit(intPoint, ray.getStartPoint(), this);
            }
        }
    }

    @Override
    public Vector3D getSurfaceNormal(Vector3D point) {
        // transform point
        // need orientation to be (0,0,1) for calculation
        double zAngle;
        double yAngle = Math.acos(orientation.z);

        if( orientation.x == 0 && orientation.y == 0 ) zAngle = 0.0;
        else zAngle = Math.acos(orientation.x / Math.sqrt(Math.pow(orientation.x,2.0) + Math.pow(orientation.y,2.0)));

        Vector3D tPoint = point.subtract(position).rotateZ(zAngle).rotateY(yAngle);

        double r = Math.sqrt( Math.pow(tPoint.x, 2.0) + Math.pow(tPoint.y, 2.0) );

        // if point not in R distance, it has to be on top or bottom -- decide from z component
        Vector3D normal;
        if( Math.abs(R-r) > 1e-4 ) {
            if( tPoint.z > 0.0 ) normal = new Vector3D(0.0,0.0,1.0);
            else normal = new Vector3D(0.0,0.0,-1.0);
        } else normal = (new Vector3D(tPoint.x, tPoint.y, 0.0)).normalize();

        //transform back
        Vector3D N = normal.rotateY(-yAngle).rotateZ(-zAngle);

        return N;
    }

    @Override
    public Vector3D getSurfaceColor(Vector3D point) {
        return surfaceColor;
    }
}
