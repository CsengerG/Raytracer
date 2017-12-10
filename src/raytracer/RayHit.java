package raytracer;

public class RayHit {
    private Vector3D hitPosition;
    private Vector3D surfaceNormal;
    private Vector3D origin;
    private Shape objectHit;

    public RayHit(Vector3D hitp, Vector3D surfn, Vector3D O, Shape objh) {
        hitPosition = hitp;
        origin = O;
        surfaceNormal = surfn;

        objectHit = objh;
    }

    public Vector3D getHitPosition() {
        return hitPosition;
    }

    public Vector3D getSurfaceNormal() {
        return surfaceNormal;
    }

    public Vector3D getOrigin(){
        return origin;
    }

    public Shape getObjectHit() {
        return objectHit;
    }
}
