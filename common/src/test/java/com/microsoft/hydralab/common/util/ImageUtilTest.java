package com.microsoft.hydralab.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtilTest {

    @Test
    public void testScaleBufferedImage() {
        // Create a test BufferedImage
        BufferedImage before = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        // Call the method to be tested
        BufferedImage after = ImageUtil.scaleBufferedImage(before, 0.5);

        // Assert the dimensions of the scaled image
        Assert.assertEquals(50, after.getWidth());
        Assert.assertEquals(50, after.getHeight());
    }

    @Test
    public void testWriteBufferedImageToFile() throws IOException {
        // Create a test BufferedImage
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        // Create a test output file
        File outputFile = new File("test.jpg");

        // Call the method to be tested
        boolean result = ImageUtil.writeBufferedImageToFile(image, outputFile);

        // Assert that the image was successfully written to the file
        Assert.assertTrue(result);

        // Clean up the test file
        outputFile.delete();
    }

    @Test
    public void testJoinImages() {
        // Create a test output file directory
        File outputFileDir = new File("output");

        // Create a test list of input files
        List<File> files = new ArrayList<>();
        files.add(new File("image1.jpg"));
        files.add(new File("image2.jpg"));

        // Call the method to be tested
        File outputFile = ImageUtil.joinImages(outputFileDir, "output.jpg", files);

        // Assert that the output file exists
        Assert.assertTrue(outputFile.exists());

        // Clean up the test output file and directory
        outputFile.delete();
        outputFileDir.delete();
    }
}