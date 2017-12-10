package raytracer;

public class Ray {
    private Vector3D startPoint;
    private Vector3D direction;

    public Ray(Vector3D pos, Vector3D dir){
        startPoint = pos;
        direction = dir.normalize();
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Vector3D getStartPoint() {
        return startPoint;
    }
}
