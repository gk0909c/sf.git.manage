package gk0909c.sf.git.manage.zip;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import gk0909c.sf.git.manage.test.helper.TestHelper;
import gk0909c.sf.git.manage.zip.ZipCreater;

public class TestZipCreater {

	@Test
	/* This test is Cutting corners... */
	public void testCreateZip01() throws IOException {
		// prepare
		File currentDirectory = new File(".");
		File baseDir = new File(currentDirectory.getAbsolutePath() + "/ziptest");
		baseDir.mkdir();
		File subDir = new File(baseDir.getAbsolutePath() + "/subDir");
		subDir.mkdir();
		File subDir2 = new File(baseDir.getAbsolutePath() + "/subDir2");
		subDir2.mkdir();
		File file1 = new File(baseDir.getAbsolutePath() + "/file1.txt");
		file1.createNewFile();
		File file2 = new File(subDir.getAbsolutePath() + "/file2.txt");
		file2.createNewFile();
		
		// run and assert
		ZipCreater helper = new ZipCreater();
		File zipFile = new File(currentDirectory.getAbsolutePath() + "/test.zip");
		String zipBase = currentDirectory.getAbsolutePath() + "/ziptest";
		
		helper.createZip(zipFile.getAbsolutePath(), zipBase);
		
		assertTrue(zipFile.exists());
		
		// clear prepared files
		zipFile.delete();
		TestHelper.delete(baseDir);
	}

}
