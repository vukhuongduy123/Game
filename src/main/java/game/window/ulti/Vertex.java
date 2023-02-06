package game.window.ulti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class Vertex {
    public Vertex(Position position, RGBA rgba /*, UVCoordinate uvCoordinate*/) {
        this.position = position;
        this.rgba = rgba;
        //this.uvCoordinate = uvCoordinate;
    }

    public static final int POS_2_SIZE = 2;
    public static final int COLOR_SIZE = 4;
    public static final int POS_OFFSET = 0;
    public static final int COLOR_OFFSET = POS_OFFSET + POS_2_SIZE * Float.BYTES;
    public static final int VERTEX_2_SIZE = 6;
    public static final int VERTEX_2_SIZE_BYTES = VERTEX_2_SIZE * Float.BYTES;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Position {
        private float[] pos;

        public Position(float[] pos) {
            this.pos = pos;
        }
    }

/*    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class UVCoordinate {
        private int x, y;
    }*/

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RGBA {
        private float red, green, blue, alpha;
    }

    private Position position;
    private RGBA rgba;

    //private UVCoordinate uvCoordinate;

    public static int getNumElements(Vertex vertex) {
        return 4 + vertex.getPosition().pos.length;
    }
}
