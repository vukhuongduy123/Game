package game.key;

import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@Setter
@Getter
public class KeyListener {

    private boolean[] keyPressed = new boolean[350];
    private static KeyListener keyListener = null;

    private KeyListener() {}

    public static KeyListener getInstance() {
        if (keyListener == null) {
            keyListener = new KeyListener();
        }
        return keyListener;
    }

    public static void keyCallback(long windowHandle, int key, int scancode, int action,
            int mods) {
        KeyListener listener = KeyListener.getInstance();
        if (key >= listener.getKeyPressed().length) {
            return;
        }

        if (action == GLFW_PRESS) {
            listener.getKeyPressed()[key] = true;
        } else if (action == GLFW_RELEASE) {
            listener.getKeyPressed()[key] = false;
        }
    }
}
