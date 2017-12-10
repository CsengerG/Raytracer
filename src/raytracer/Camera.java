package raytracer;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Camera {
    private Vector3D direction;
    private Vector3D up_direction;
    private Vector3D position;
    private int width, height;

    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public Vector3D getPosition() { return position; }

    public Camera(Element e){
        System.out.println("Putting a camera to the scene");

        for(Attribute attr: e.getAttributes()){
            switch(attr.getName()){
                case "position":
                    position = new Vector3D(attr.getValue());
                    break;
                case "direction":
                    direction = new Vector3D(attr.getValue());
                    direction = direction.normalize();
                    break;
                case "up-direction":
                    up_direction = new Vector3D(attr.getValue());
                    up_direction = up_direction.normalize();
                    break;
                case "width":
                    width = Integer.parseInt(attr.getValue());
                    break;
                case "height":
                    height = Integer.parseInt(attr.getValue());
                    break;
            }
        }
    }

    public List<Ray>[][] shootRays(){
        Vector3D screenCenter = position.add(direction);
        Vector3D up_perpendicular = direction.scale(direction.dot(up_direction)).subtract(up_direction).normalize();
        Vector3D right_perpendicular = direction.cross(up_perpendicular).normalize();

        Vector3D up_unit = up_perpendicular.scale(1.0/(height-1));
        Vector3D right_unit = right_perpendicular.scale(1.0/(width-1));

        double toLeft = (double)(width-1)/2.0;
        double toDown = (double)(height-1)/2.0;
        Vector3D bottomLeft = screenCenter.subtract(right_unit.scale(toLeft)).subtract(up_unit.scale(toDown));

        List<Ray>[][] shootedRays = new List[width][height];

        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                shootedRays[x][y] = new ArrayList<>();

                Vector3D pixelCenter = bottomLeft.add(right_unit.scale(x)).add(up_unit.scale(y));
                Vector3D rayDirection = pixelCenter.subtract(position).normalize();

                // shoot through center of pixel
                shootedRays[x][y].add(new Ray(position, rayDirection));

                // shoot several other rays
                for(int i = 0; i < 8; ++i) {
                    Vector3D xRadius = right_unit.scale(0.5);
                    Vector3D yRadius = up_unit.scale(0.5);

                    double xRandom = Math.random()*2 - 1.0;
                    double yRandom = Math.random()*2 - 1.0;

                    Vector3D randomPointInPixel = pixelCenter.add( xRadius.scale(xRandom) ).add( yRadius.scale(yRandom) );
                    Vector3D randomRayDirection = randomPointInPixel.subtract(position).normalize();

                    shootedRays[x][y].add(new Ray(position, randomRayDirection) );
                }
            }
        }

        return shootedRays;
    }
}
