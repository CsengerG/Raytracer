package raytracer;

import org.jdom2.JDOMException;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Raytracer {

    public static void main(String[] args) throws JDOMException, IOException {
        Scene scene = new Scene("scene.xml");

        writeArrayToImage(scene.getPixels(), "output.png");
    }

    public static void writeArrayToImage(int[][] pixels, String filename) throws IOException {
        int width = pixels.length;
        int height = pixels[0].length;

        BufferedImage img = new BufferedImage(width, height, TYPE_3BYTE_BGR);

        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                img.setRGB(x, y, pixels[x][y]);
            }
        }

        ImageIO.write(img, "PNG", new File(filename));
    }
}
