package game.window.render;

import game.GameMain;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
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
        // Bind shader program
        glUseProgram(shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }
}
