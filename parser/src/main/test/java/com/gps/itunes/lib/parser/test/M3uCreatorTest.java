package com.gps.itunes.lib.parser.test;

import com.gps.itunes.lib.tasks.M3uCreator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Created by leogps on 3/29/15.
 */
@Test
public class M3uCreatorTest {

    @Test
    public void testRelativePath() {

        String relativePath = M3uCreator.getRelativePath(new File("/Users/leogps/Desktop/Music/Purchased-13476"),
                new File("/Users/leogps/Desktop/Music/Purchased-16224/a/b/c/d/e/f/g/h/i/j/k/l/m/n/01 Rolling in the Deep.m4a"));

//        System.out.println(relativePath);

        Assert.assertNotNull(relativePath);
        Assert.assertEquals(relativePath, "../Purchased-16224/a/b/c/d/e/f/g/h/i/j/k/l/m/n/01 Rolling in the Deep.m4a");
    }

    @Test
    public void testRelativePathWithSameDirectory() {
        String relativePath = M3uCreator.getRelativePath(new File("/Users/leogps/Desktop/Music/Purchased-16224/a/b/c/d/e/f/g/h/i/j/k/l/m/n/"),
                new File("/Users/leogps/Desktop/Music/Purchased-16224/a/b/c/d/e/f/g/h/i/j/k/l/m/n/01 Rolling in the Deep.m4a"));

//        System.out.println(relativePath);

        Assert.assertNotNull(relativePath);
        Assert.assertEquals(relativePath, "01 Rolling in the Deep.m4a");
    }

}
