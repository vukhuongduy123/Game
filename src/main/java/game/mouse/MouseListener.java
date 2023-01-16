package game.mouse;

import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@Setter
@Getter
public class MouseListener {
    private double xScroll, yScroll;
    private double xPos, yPos, lastX, lastY;

    // 1: left; 2:right; 3:middle
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;
    private static MouseListener mouseListener = null;

    private MouseListener() {}

    public static MouseListener getInstance() {
        if (mouseListener == null) {
            mouseListener = new MouseListener();
        }
        return mouseListener;
    }

    public static void mousePosCallback(long windowHandle, double x, double y) {
        MouseListener listener = MouseListener.getInstance();
        listener.setXPos(listener.getLastX());
        listener.setXPos(listener.getLastX());
        listener.setXPos(x);
        listener.setYPos(y);
        boolean isDragging = false;
        for (boolean isButtonDragging : listener.mouseButtonPressed) {
            isDragging = isDragging || isButtonDragging;
        }
        listener.setDragging(isDragging);
    }

    public static void mouseButtonCallback(long windowHandle, int button, int action, int mods) {
        MouseListener listener = MouseListener.getInstance();
        if (button >= listener.getMouseButtonPressed().length) {
            return;
        }

        if (action == GLFW_PRESS) {
            listener.getMouseButtonPressed()[button] = true;
        } else if (action == GLFW_RELEASE) {
            listener.getMouseButtonPressed()[button] = false;
            listener.isDragging = false;
        }
    }

    public static void mouseScrollCallback(long windowHandle, double xOffset, double yOffset) {
        MouseListener listener = MouseListener.getInstance();
        listener.setXScroll(xOffset);
        listener.setYScroll(yOffset);

    }
}
