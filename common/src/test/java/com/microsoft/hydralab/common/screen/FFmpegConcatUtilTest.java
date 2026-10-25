package com.microsoft.hydralab.common.screen;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FFmpegConcatUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(FFmpegConcatUtilTest.class);

    @Test
    public void testConcatVideos() {
        List<File> videos = new ArrayList<>();
        File video1 = new File("path/to/video1.mp4");
        File video2 = new File("path/to/video2.mp4");
        videos.add(video1);
        videos.add(video2);

        File outputDir = new File("path/to/output");
        File result = FFmpegConcatUtil.concatVideos(videos, outputDir, logger);

        Assert.assertNotNull(result);
        Assert.assertEquals("output/filename.mp4", result.getAbsolutePath());
    }

    @Test
    public void testMergeVideosSideBySide() {
        String leftVideoPath = "path/to/leftVideo.mp4";
        String rightVideoPath = "path/to/rightVideo.mp4";
        String mergeDestinationPath = "path/to/mergeDestination.mp4";

        FFmpegConcatUtil.mergeVideosSideBySide(leftVideoPath, rightVideoPath, mergeDestinationPath, logger);

        File mergedVideo = new File(mergeDestinationPath);
        Assert.assertTrue(mergedVideo.exists());
    }

    @Test
    public void testMergeVideosSideBySide_multipleVideos() {
        List<String> videoPaths = new ArrayList<>();
        videoPaths.add("path/to/video1.mp4");
        videoPaths.add("path/to/video2.mp4");
        videoPaths.add("path/to/video3.mp4");

        File outputDir = new File("path/to/output");
        File result = FFmpegConcatUtil.mergeVideosSideBySide(videoPaths, outputDir, logger);

        Assert.assertNotNull(result);
        Assert.assertEquals("output/filename.mp4", result.getAbsolutePath());
    }
}