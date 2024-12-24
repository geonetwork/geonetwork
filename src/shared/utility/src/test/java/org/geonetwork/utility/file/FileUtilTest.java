/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import org.junit.Assert;
import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testTextFileContentType() throws IOException, URISyntaxException {
        Path path = this.getPathFromResource("test.txt");
        String fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("text/plain", fileType);
    }

    @Test
    public void testHtmlFileContentType() throws IOException, URISyntaxException {
        Path path = this.getPathFromResource("test.html");
        String fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("text/html", fileType);
    }

    @Test
    public void testXmlFileContentType() throws IOException, URISyntaxException {
        Path path = this.getPathFromResource("test.xml");
        String fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("application/xml", fileType);
    }

    @Test
    public void testImageFileContentType() throws IOException, URISyntaxException {
        Path path = this.getPathFromResource("test.jpeg");
        String fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("image/jpeg", fileType);

        path = this.getPathFromResource("test.png");
        fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("image/png", fileType);
    }

    @Test
    public void testJsonFileContentTypes() throws IOException, URISyntaxException {
        Path path = this.getPathFromResource("test.json");
        String fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("application/json", fileType);
    }

    @Test
    public void testMefFileContentTypes() throws IOException, URISyntaxException {
        Path path = this.getPathFromResource("test.mef");
        String fileType = FileUtil.getFileContentType(path);
        Assert.assertEquals("application/mef", fileType);
    }

    private Path getPathFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI()).toPath();
        }
    }
}
