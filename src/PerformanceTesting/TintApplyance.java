package PerformanceTesting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

// 41533800
// 37376100

// code in this test was written by ChatGPT to speed up performance
// testing to choose which method of sprite tinting the framework should use

public class TintApplyance {
    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        Color tint = new Color(255, 0, 0);  // Red tint
        long startTime, endTime;

        // Benchmark RescaleOp
        startTime = System.nanoTime();
        BufferedImage tintedImageRescaleOp = applyTintUsingRescaleOp(image, tint);
        endTime = System.nanoTime();
        System.out.println("RescaleOp time: " + (endTime - startTime) + " nanoseconds");

        // Benchmark Manual Pixel Manipulation
        startTime = System.nanoTime();
        BufferedImage tintedImageManual = applyTintManually(image, tint);
        endTime = System.nanoTime();
        System.out.println("Manual pixel manipulation time: " + (endTime - startTime) + " nanoseconds");
    }

    public static BufferedImage applyTintUsingRescaleOp(BufferedImage image, Color tint) {
        float scaleFactorR = tint.getRed() / 255.0f;
        float scaleFactorG = tint.getGreen() / 255.0f;
        float scaleFactorB = tint.getBlue() / 255.0f;
        float[] scales = {scaleFactorR, scaleFactorG, scaleFactorB, 1.0f};
        float[] offsets = {0.0f, 0.0f, 0.0f, 0.0f};

        RescaleOp op = new RescaleOp(scales, offsets, null);
        return op.filter(image, null);
    }

    public static BufferedImage applyTintManually(BufferedImage image, Color tint) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage tintedImage = new BufferedImage(width, height, image.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelColor = image.getRGB(x, y);
                int alpha = (pixelColor >> 24) & 0xFF;
                int red = (pixelColor >> 16) & 0xFF;
                int green = (pixelColor >> 8) & 0xFF;
                int blue = pixelColor & 0xFF;

                red = (int)(red * tint.getRed() / 255.0f);
                green = (int)(green * tint.getGreen() / 255.0f);
                blue = (int)(blue * tint.getBlue() / 255.0f);

                int tintedColor = (alpha << 24) | (red << 16) | (green << 8) | blue;
                tintedImage.setRGB(x, y, tintedColor);
            }
        }
        return tintedImage;
    }
}
