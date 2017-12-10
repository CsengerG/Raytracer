package raytracer;

import org.jdom2.Element;

public class Torus extends Shape {
    public Torus(Element e){
        super(e);
        System.out.println("Putting a torus to the scene");
    }

    public RayHit intersection(Ray ray) {
        return null;
    }

    public Vector3D getSurfaceNormal(Vector3D point) {
        return null;
    }
}
