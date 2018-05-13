package pl.android.puzzledepartment.util.geometry;

/**
 * Created by Maciek Ruszczyk on 2017-10-06.
 */

public class Vector3f {
    public final float x, y, z;

    public Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public float length(){
        return (float) Math.sqrt(x*x + y*y + z*z);
    }
    public Vector3f crossProduct(Vector3f other){
        return new Vector3f(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }
    public float dotProduct(Vector3f vector){
        return x * vector.x + y * vector.y + z * vector.z;
    }
    public Vector3f scale(float f){
        return new Vector3f(x*f, y*f, z*f);
    }

    public Vector3f normalize(){
        final float len = this.length();
        return new Vector3f(x/len, y/len, z/len);
    }
}
