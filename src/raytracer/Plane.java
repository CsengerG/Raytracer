package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Plane extends Shape {
    private Vector3D normal;
    private double distance;

    public Plane(Element e){
        super(e);
        System.out.println("Putting a plane to the scene");

        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "normal":
                    normal = new Vector3D(attr.getValue());
                    break;
                case "distance":
                    distance = Double.parseDouble(attr.getValue());
                    break;
            }
        }
    }

    public RayHit intersection(Ray ray) {
        Vector3D n = normal;
        double d = distance;

        Vector3D p = ray.getStartPoint();
        Vector3D u = ray.getDirection();

        double lambda = (d - p.dot(n)) / u.dot(n);

        if( lambda <= 0.0 ) return null;

        Vector3D intPoint = u.scale(lambda).add(p);

        return new RayHit(intPoint, this.getSurfaceNormal(intPoint), ray.getStartPoint(), this);
    }

    public Vector3D getSurfaceNormal(Vector3D point) {
        return normal;
    }
}
