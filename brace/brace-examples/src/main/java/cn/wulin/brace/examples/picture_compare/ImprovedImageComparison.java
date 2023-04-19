package cn.wulin.brace.examples.picture_compare;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImprovedImageComparison {
	private final static String ROOT_PATH = System.getProperty("user.dir")+"/config/resources";

    public static void main(String[] args) throws IOException {
    	String aPath = ROOT_PATH+"/images/a.png";
    	String bPath = ROOT_PATH+"/images/b.png";
    	String differencePath = ROOT_PATH+"/images/difference.png";
    	String mergebPath = ROOT_PATH+"/images/mergeb.png";
    	
        BufferedImage imgA = readImage(aPath);
        BufferedImage imgB = readImage(bPath);

        BufferedImage mask = compareImages(imgA, imgB);
        ImageIO.write(mask, "PNG", new File(differencePath));

        BufferedImage mergedImage = mergeImages(imgA, imgB, mask);
        ImageIO.write(mergedImage, "PNG", new File(mergebPath));
    }

    public static BufferedImage readImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    public static BufferedImage compareImages(BufferedImage imgA, BufferedImage imgB) {
        int width = imgA.getWidth();
        int height = imgA.getHeight();

        BufferedImage mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelA = imgA.getRGB(x, y);
                int pixelB = imgB.getRGB(x, y);

                if (pixelA != pixelB) {
                    mask.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    mask.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return mask;
    }

    public static BufferedImage mergeImages(BufferedImage imgA, BufferedImage imgB, BufferedImage mask) {
        int width = imgA.getWidth();
        int height = imgA.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int maskPixel = mask.getRGB(x, y);

                if (maskPixel == Color.WHITE.getRGB()) {
                    result.setRGB(x, y, imgB.getRGB(x, y));
                } else {
                    result.setRGB(x, y, imgA.getRGB(x, y));
                }
            }
        }

        return result;
    }
}
