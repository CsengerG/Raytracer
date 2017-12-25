package raytracer;

import org.jdom2.Element;

public class Cube extends Shape {
    public Cube(Element e){
        super(e);
        System.out.println("Putting a cube to the scene");
    }

    public RayHit intersection(Ray ray) {
        return null;
    }

    public Vector3D getSurfaceNormal(Vector3D point) {
        return null;
    }

    @Override
    public Vector3D getSurfaceColor(Vector3D point) {
        return surfaceColor;
    }
}
