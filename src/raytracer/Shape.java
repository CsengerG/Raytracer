package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public abstract class Shape {
    protected Vector3D surfaceColor;
    protected double diffuseCoefficient;
    protected double specularCoefficient;
    protected double alpha;
    protected double reflectiveCoefficient;

    public abstract RayHit intersection(Ray ray);
    public abstract Vector3D getSurfaceNormal(Vector3D point);

    public Shape(Element e){
        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "color":
                    surfaceColor = Vector3D.hexToColorVector(attr.getValue());
                    break;
                case "diffuse-coefficient":
                    diffuseCoefficient = Double.parseDouble(attr.getValue());
                    break;
                case "specular-coefficient":
                    specularCoefficient = Double.parseDouble(attr.getValue());
                case "alpha":
                    alpha = Double.parseDouble(attr.getValue());
                case "reflective-coefficient":
                    reflectiveCoefficient = Double.parseDouble(attr.getValue());
            }
        }
    }

    public abstract Vector3D getSurfaceColor(Vector3D point);

    public double getDiffuseCoefficient() {
        return diffuseCoefficient;
    }

    public double getSpecularCoefficient() {
        return specularCoefficient;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getReflectiveCoefficient() {
        return reflectiveCoefficient;
    }
}
