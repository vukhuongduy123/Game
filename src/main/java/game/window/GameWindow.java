package game.window;

import game.window.scene.LevelEditorScene;
import game.window.scene.LevelScene;
import game.window.scene.Scene;
import game.window.key.KeyListener;
import game.window.mouse.MouseListener;
import game.window.ulti.Time;
import game.window.ulti.Vertex;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

@Getter
@Setter
public class GameWindow {
    private final int height, width;
    private final String title;
    private long windowHandle;
    private static GameWindow gameWindow;
    private Vertex.RGBA color;
    private Scene currentScene;

    private GameWindow() {
        height = 1366;
        width = 768;
        title = "Mario";
        color = new Vertex.RGBA(1, 1, 1, 1);
    }

    private void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetMouseButtonCallback(windowHandle, MouseListener::mouseButtonCallback);
        glfwSetCursorPosCallback(windowHandle, MouseListener::mousePosCallback);
        glfwSetScrollCallback(windowHandle, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(windowHandle, KeyListener::keyCallback);

        // Set up a key callback. It will be called every time a key is pressed, repeated or
        // released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1); // int*
            IntBuffer h = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, w, h);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidMode != null;
            glfwSetWindowPos(
                    windowHandle,
                    (vidMode.width() - w.get(0)) / 2,
                    (vidMode.height() - h.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        changeScene(0);
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(windowHandle)) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            // Set the clear color
            glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            if (dt >= 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(windowHandle); // swap the color buffers


            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static GameWindow getInstance() {
        if (gameWindow == null) {
            gameWindow = new GameWindow();
        }
        return gameWindow;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void changeScene(int scene) {
        switch (scene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.inti();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.inti();
                break;
            default:
                assert false : "Unknown scene: " + scene + System.lineSeparator();
        }
    }
}
