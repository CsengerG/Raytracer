package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Light {
    private Vector3D position;
    private Vector3D lightColor;

    public Light(Element e){
        System.out.println("Putting a light to the scene");

        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "position":
                    position = new Vector3D(attr.getValue());
                    break;
                case "color":
                    lightColor = Vector3D.hexToColorVector(attr.getValue());
                    break;
            }
        }
    }

    public Vector3D getLightColor() {
        return lightColor;
    }

    public Vector3D getPosition() {
        return position;
    }
}
