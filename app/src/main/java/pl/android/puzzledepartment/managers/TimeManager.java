package pl.android.puzzledepartment.managers;

/**
 * Created by Maciek Ruszczyk on 2017-10-20.
 */

public class TimeManager {

    private static long timeFromBeginningInSeconds = System.nanoTime();
    private static long lastFrameTime = System.nanoTime();
    private static float deltaInSeconds;

    public static float getDeltaTimeInSeconds() {
        return deltaInSeconds;
    }

    public static float getElapsedTimeFromBeginningInSeconds() {
        return (System.nanoTime() - timeFromBeginningInSeconds) / 1000000000f;
    }

    public static void update() {
        long currentFrameTime =  System.nanoTime();
        deltaInSeconds = (currentFrameTime - lastFrameTime) / 1000000000f;
        lastFrameTime = currentFrameTime;
    }

    public static void start() {
        timeFromBeginningInSeconds = System.nanoTime();
        lastFrameTime = System.nanoTime();
    }
}
