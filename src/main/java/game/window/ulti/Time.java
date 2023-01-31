package game.window.ulti;

public class Time {
    public static float timeStart = System.nanoTime();
    public static float getTime() {
        return (System.nanoTime() - timeStart) * 1E-9f;
    }
}
