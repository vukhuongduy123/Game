package game.window.ulti;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public class Conversion {
    public static float[] convertToFloatArray(List<Pair<Position, RGBA>> list) {
        float[] floats = new float[list.size() * 7];
        for (int i = 0; i < list.size(); i++) {
            floats[i * 7] = list.get(i).getFirst().getX();
            floats[i * 7 + 1] = list.get(i).getFirst().getY();
            floats[i * 7 + 2] = list.get(i).getFirst().getZ();
            floats[i * 7 + 3] = list.get(i).getSecond().getRed();
            floats[i * 7 + 4]  = list.get(i).getSecond().getGreen();
            floats[i * 7 + 5]  = list.get(i).getSecond().getBlue();
            floats[i * 7 + 6] = list.get(i).getSecond().getAlpha();
        }

        return floats;
    }
}
