package pl.android.puzzledepartment.util.geometry;

/**
 * Created by Maciek Ruszczyk on 2017-10-07.
 */

public class Vector2f {
    public final float x, y;

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }
    public float length(){
        return (float) Math.sqrt(x*x + y*y);
    }
    public float dotProduct(Vector2f vector){
        return x * vector.x + y * vector.y;
    }
    public Vector2f scale(float f){
        return new Vector2f(x*f, y*f);
    }

    public Vector2f normalize(){
        final float len = this.length();
        return new Vector2f(x/len, y/len);
    }
}
