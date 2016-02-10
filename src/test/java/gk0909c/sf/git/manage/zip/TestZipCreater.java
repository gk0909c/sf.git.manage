package gk0909c.sf.git.manage.zip;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import gk0909c.sf.git.manage.zip.ZipCreater;

public class TestZipCreater {
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	/* This test is Cutting corners... */
	public void testCreateZip01() throws IOException {
		// prepare
		File baseDir = tempFolder.newFolder("ziptest");
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
		File zipFile = new File(tempFolder.getRoot() + "/test.zip");
		
		helper.createZip(zipFile.getAbsolutePath(), baseDir.getAbsolutePath());
		
		assertTrue(zipFile.exists());
		
	}

}
