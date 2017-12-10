package raytracer;

import java.util.Vector;

public class Vector3D {
    public double x,y,z;

    public Vector3D(double xx, double yy, double zz) {
        x = xx;
        y = yy;
        z = zz;
    }

    public Vector3D(String vecDesc){
        String regex = "[,]+";
        String[] components = vecDesc.split(regex);

        x = Double.parseDouble(components[0]);
        y = Double.parseDouble(components[1]);
        z = Double.parseDouble(components[2]);
    }

    public Vector3D() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Vector3D add(Vector3D v) {
        return new Vector3D(v.x + x, v.y + y, v.z + z);
    }

    public Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    public double dot(Vector3D v) {
        return x*v.x + y*v.y+z*v.z;
    }

    public Vector3D scale(Vector3D v) {
        return new Vector3D(v.x*x, v.y*y, v.z*z);
    }

    public Vector3D scale(double s) {
        return new Vector3D(s*x, s*y, s*z);
    }

    public Vector3D cross(Vector3D v) {
        return new Vector3D(y*v.z - z*v.y, z*v.y - x*v.z, x*v.y - y*v.x);
    }

    public double magnitude(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3D normalize(){
        return new Vector3D(x / this.magnitude(), y / this.magnitude(), z / this.magnitude());
    }

    public Vector3D reflectIn(Vector3D v){
        return v.scale( 2 * this.dot(v) ).subtract(this);
    }

    public Vector3D addIllumination(Vector3D v){
        return new Vector3D(Double.min(v.x+x, 1.0), Double.min(v.y+y,1.0), Double.min(v.z+z,1.0));
    }

    public static Vector3D hexToColorVector(String stringRGB){
        String hexColor = stringRGB.substring(1);

        String red = hexColor.substring(0,2);
        String green = hexColor.substring(2,4);
        String blue = hexColor.substring(4,6);

        int r = Integer.parseInt(red, 16);
        int g = Integer.parseInt(green, 16);
        int b = Integer.parseInt(blue, 16);

        return new Vector3D((double) r/255.0,(double) g/255.0,(double) b/255.0);
    }

    public static int colorVectorToInt(Vector3D v){
        if(v == null) return 0;

        int red = (int)(v.x*255.0);
        int green = (int)(v.y*255.0);
        int blue = (int)(v.z*255.0);

        return red*256*256 + green*256 + blue;
    }
}