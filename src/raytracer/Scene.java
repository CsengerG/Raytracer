package raytracer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public class Scene {
    private List<Shape> sceneObjects;
    private List<Light> lights;
    private Camera camera;

    public Scene(String xmlDescriptor) {
        sceneObjects = new ArrayList<>();
        lights = new ArrayList<>();

        System.out.println("Loading XML scene description");

        try {
            File inputFile = new File(xmlDescriptor);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);

            Element rootElement = document.getRootElement();

            for(Element e: rootElement.getChildren()){
                String shapeType = e.getAttributeValue("type");

                switch (e.getName()){
                    case "shape":
                        switch (shapeType) {
                            case "sphere":
                                sceneObjects.add(new Sphere(e));
                                break;
                            case "plane":
                                sceneObjects.add(new Plane(e));
                                break;
                            case "cube":
                                sceneObjects.add(new Cube(e));
                                break;
                            case "torus":
                                sceneObjects.add(new Torus(e));
                                break;
                            default:
                                throw new RuntimeException("Can't put a " + shapeType + " on the scene");
                        }
                        break;
                    case "camera":
                        camera = new Camera(e);
                        break;
                    case "light":
                        lights.add(new Light(e));
                        break;
                    default:
                        throw new RuntimeException("Unsupported thing " + e.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong during parsing the scene XML file");
            e.printStackTrace();
        }

    }

    public int[][] getPixels() {
        int pixels[][] = new int[camera.getWidth()][camera.getHeight()];
        int numOfPixels = camera.getWidth()*camera.getHeight();

        List<Ray>[][] allRays = camera.shootRays();

        for(int x = 0; x < allRays.length; ++x){
            for(int y = 0; y < allRays[x].length; ++y){
                Vector3D color = new Vector3D();

                for(int i = 0; i < allRays[x][y].size(); ++i){
                    color = color.add(colorVectorFromRay(allRays[x][y].get(i)));
                }

                color = color.scale(1.0 / allRays[x][y].size() );
                pixels[x][y] = Vector3D.colorVectorToInt(color);
            }

            System.out.println((double) 100*x*allRays.length / numOfPixels + "%");
        }

        return pixels;
    }

    public Vector3D colorVectorFromRay(Ray ray) {
        double closest = Double.MAX_VALUE;
        RayHit closestHit = null;

        for(Shape shape: sceneObjects) {
            if (shape.intersection(ray) != null) {
                RayHit rh = shape.intersection(ray);
                double distance = rh.getHitPosition().subtract(camera.getPosition()).magnitude();

                if( distance < closest ) {
                    closest = distance;
                    closestHit = rh;
                }
            }
        }

        if( closestHit != null ) {
            return illuminate(closestHit, 5);
        }

        return new Vector3D();
    }

    public Vector3D illuminate(RayHit hit, int recursionDepth){
        if(hit == null) return new Vector3D();

        double epsilon = 0.0001;
        Vector3D Cdiff = hit.getObjectHit().getSurfaceColor();
        Vector3D Ia = new Vector3D(0.25, 0.25, 0.25);

        // add ambient
        Vector3D illumination = Cdiff.scale(Ia);

        Vector3D P = hit.getHitPosition();

        Vector3D N = hit.getObjectHit().getSurfaceNormal(P).normalize();
        Vector3D V = hit.getOrigin().subtract(P).normalize();

        // add diffuse
        for(Light currLight: lights){
            double kd = hit.getObjectHit().getDiffuseCoefficient();
            double ks = hit.getObjectHit().getSpecularCoefficient();
            double alpha = hit.getObjectHit().getAlpha();

            Vector3D I = currLight.getLightColor();
            Vector3D L = currLight.getPosition().subtract(P).normalize();
            Vector3D R = L.reflectIn(N).normalize();

            Vector3D diffComp = Cdiff.scale(I).scale( Double.max(0.0, N.dot(L)) ).scale(kd);
            Vector3D specularComp = Cdiff.scale(I).scale( Double.max(0.0, Math.pow( R.dot(V), alpha ))).scale(ks);

            double lightDist = currLight.getPosition().subtract(P).magnitude();
            Ray r = new Ray( L.scale(epsilon).add(P), L );

            double closest = Double.MAX_VALUE;

            for(Shape shape: sceneObjects){
                if( shape.intersection(r) != null ) {
                    Vector3D intPoint = shape.intersection(r).getHitPosition();

                    closest = Double.min(closest, intPoint.subtract(P).magnitude());
                }
            }

            if(closest > lightDist) illumination = illumination.addIllumination(diffComp).addIllumination(specularComp);
        }

        if( recursionDepth == 0 ) return illumination;
        else {
            // create reflected ray
            Vector3D Vr = V.reflectIn(N).normalize();
            Ray refRay = new Ray(P.add( Vr.scale(epsilon) ), Vr);
            Vector3D refIllum = illuminate(getClosestIntersection(refRay), recursionDepth-1);
            double refCoef = hit.getObjectHit().getReflectiveCoefficient();

            return illumination.scale(1-refCoef).addIllumination(refIllum.scale(refCoef));
        }
    }

    public RayHit getClosestIntersection(Ray ray){
        double closest = Double.MAX_VALUE;
        RayHit rh = null;

        for(Shape shape: sceneObjects){
            if( shape.intersection(ray) != null ) {
                Vector3D intPoint = shape.intersection(ray).getHitPosition();
                double dist = intPoint.subtract(ray.getStartPoint()).magnitude();

                if( dist < closest ){
                    closest = dist;
                    rh = new RayHit(intPoint, shape.getSurfaceNormal(intPoint), ray.getStartPoint(), shape);
                }
            }
        }

        return rh;
    }
}
