package com.microsoft.hydralab.common.entity.common;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class StorageFileInfoTest {

    @Test
    public void testConstructorWithFileAndRelativeParentAndFileTypeAndLoadTypeAndLoadDir() {
        File file = new File("test.txt");
        String relativeParent = "parent";
        String fileType = "fileType";
        String loadType = "loadType";
        String loadDir = "loadDir";

        StorageFileInfo storageFileInfo = new StorageFileInfo(file, relativeParent, fileType, loadType, loadDir);

        Assert.assertEquals(fileType, storageFileInfo.getFileType());
        Assert.assertEquals("test.txt", storageFileInfo.getFileName());
        Assert.assertEquals(0, storageFileInfo.getFileLen());
        Assert.assertEquals("parent/test.txt", storageFileInfo.getBlobPath());
        Assert.assertNull(storageFileInfo.getBlobContainer());
        Assert.assertNull(storageFileInfo.getMd5());
        Assert.assertNull(storageFileInfo.getCreateTime());
        Assert.assertNull(storageFileInfo.getUpdateTime());
        Assert.assertNull(storageFileInfo.getCDNUrl());
    }

    @Test
    public void testConstructorWithFileAndRelativeParentAndFileType() {
        File file = new File("test.txt");
        String relativeParent = "parent";
        String fileType = "fileType";

        StorageFileInfo storageFileInfo = new StorageFileInfo(file, relativeParent, fileType);

        Assert.assertEquals(fileType, storageFileInfo.getFileType());
        Assert.assertEquals("test.txt", storageFileInfo.getFileName());
        Assert.assertEquals(0, storageFileInfo.getFileLen());
        Assert.assertEquals("parent/test.txt", storageFileInfo.getBlobPath());
        Assert.assertNull(storageFileInfo.getBlobContainer());
        Assert.assertNull(storageFileInfo.getMd5());
        Assert.assertNull(storageFileInfo.getCreateTime());
        Assert.assertNull(storageFileInfo.getUpdateTime());
        Assert.assertNull(storageFileInfo.getCDNUrl());
    }

    @Test
    public void testConstructorWithFileAndFileRelPathAndFileTypeAndEntityType() {
        File file = new File("test.txt");
        String fileRelPath = "relPath";
        String fileType = "fileType";

        StorageFileInfo storageFileInfo = new StorageFileInfo(file, fileRelPath, fileType);

        Assert.assertEquals(fileType, storageFileInfo.getFileType());
        Assert.assertEquals("test.txt", storageFileInfo.getFileName());
        Assert.assertEquals(0, storageFileInfo.getFileLen());
        Assert.assertEquals("relPath", storageFileInfo.getBlobPath());
        Assert.assertNull(storageFileInfo.getBlobContainer());
        Assert.assertNull(storageFileInfo.getMd5());
        Assert.assertNull(storageFileInfo.getCreateTime());
        Assert.assertNull(storageFileInfo.getUpdateTime());
        Assert.assertNull(storageFileInfo.getCDNUrl());
    }
}