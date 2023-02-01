package game.window.render;

import game.GameMain;
import lombok.SneakyThrows;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private boolean isUsed = false;
    private int shaderProgramId;
    private final String vertexSrc, fragmentSrc;

    @SneakyThrows
    public Shader(String vertexPath, String fragmentPath) {
        vertexSrc = new String(Objects.requireNonNull(
                GameMain.class.getResourceAsStream(vertexPath)).readAllBytes(),
                               StandardCharsets.UTF_8);
        fragmentSrc = new String(Objects.requireNonNull(
                GameMain.class.getResourceAsStream(fragmentPath)).readAllBytes(),
                                 StandardCharsets.UTF_8);
    }

    public void compile() {
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("ERROR: default vertex shader error");
            assert false : glGetShaderInfoLog(vertexID, glGetShaderi(vertexID, GL_INFO_LOG_LENGTH));
        }

        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("ERROR: default fragment shader error");
            assert false : glGetShaderInfoLog(fragmentID,
                                              glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH));
        }

        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexID);
        glAttachShader(shaderProgramId, fragmentID);
        glLinkProgram(shaderProgramId);

        if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println("ERROR: default linking shader error");
            System.out.println(glGetProgramInfoLog(shaderProgramId, glGetProgrami(shaderProgramId,
                                                                                  GL_INFO_LOG_LENGTH)));
            assert false : glGetProgramInfoLog(shaderProgramId,
                                               glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH));
        }

    }

    public void use() {
        if (!isUsed) {
            // Bind shader program
            glUseProgram(shaderProgramId);
            isUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        isUsed = false;
    }

    public void uploadMatrix(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(shaderProgramId, name);
        use();
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(4 * 4);
        matrix.get(floatBuffer);
        glUniformMatrix4fv(location, false, floatBuffer);
    }

    public void uploadVector(String name, Vector4f vec) {
        int location = glGetUniformLocation(shaderProgramId, name);
        use();
        glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadFloat(String name, float val) {
        int varLocation = glGetUniformLocation(shaderProgramId, name);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String name, int val) {
        int varLocation = glGetUniformLocation(shaderProgramId, name);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadVec3f(String name, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, name);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, name);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }
}
