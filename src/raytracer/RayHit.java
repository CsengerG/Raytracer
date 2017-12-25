package raytracer;

public class RayHit {
    private Vector3D hitPosition;
    private Vector3D origin;
    private Shape objectHit;
    private boolean hit;

    public RayHit(Vector3D hitp, Vector3D O, Shape objh) {
        hitPosition = hitp;
        origin = O;
        hit = true;

        objectHit = objh;
    }

    public RayHit() {
        hit = false;
    }

    public Vector3D getHitPosition() {
        return hitPosition;
    }

    public Vector3D getOrigin(){
        return origin;
    }

    public Shape getObjectHit() {
        return objectHit;
    }

    public boolean doesExist(){
        return hit;
    }
}
