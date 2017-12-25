package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Plane extends Shape {
    private Vector3D normal;
    private double distance;
    private Texture texture;

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
                case "texture":
                    texture = new Texture(attr.getValue());
                    break;
            }
        }
    }

    @Override
    public Vector3D getSurfaceColor(Vector3D point) {
        if(texture == null) return surfaceColor;

        Vector3D O = normal.scale(distance);
        Vector3D u = new Vector3D(normal.z-normal.y, normal.x-normal.z, normal.y-normal.x).normalize();
        Vector3D v = new Vector3D(normal.x*(normal.y+normal.z)-normal.y*normal.y-normal.z*normal.z,
                normal.y*(normal.x+normal.z)-normal.x*normal.x-normal.z*normal.z,
                normal.z*(normal.x+normal.y)-normal.x*normal.x-normal.y*normal.y).normalize();

        Vector3D w = point.subtract(O);

        double b = (w.y*u.x + w.x*u.y) / (u.y*v.x + u.x*v.y);
        double a = (b*v.x-w.x) / u.x;

        System.out.println(a + " " + b);

        return surfaceColor;
    }

    public RayHit intersection(Ray ray) {
        Vector3D n = normal;
        double d = distance;

        Vector3D p = ray.getStartPoint();
        Vector3D u = ray.getDirection();

        double lambda = (d - p.dot(n)) / u.dot(n);

        if( lambda <= 0.0 ) return new RayHit();

        Vector3D intPoint = u.scale(lambda).add(p);

        return new RayHit(intPoint, ray.getStartPoint(), this);
    }

    public Vector3D getSurfaceNormal(Vector3D point) {
        return normal;
    }
}
