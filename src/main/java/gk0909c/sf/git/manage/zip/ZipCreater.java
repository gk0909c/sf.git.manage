package gk0909c.sf.git.manage.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.utils.IOUtils;

public class ZipCreater {
	private static final String PREFIX_DIR = "unpackaged/";
	/**
	 * make zip file.
	 * This method put automatically into "unpackaged" directory baseDir.
	 * @param zipFile zip file path
	 * @param baseDir archived base
	 * @throws IOException
	 * @throws ArchiveException 
	 * @throws CompressorException 
	 */
	public void createZip(String zipFile, String baseDir) throws IOException {
		File baseDirFile = new File(baseDir);
		
		// Zip ファイル作成
		OutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
		
		ZipArchiveOutputStream archive = new ZipArchiveOutputStream(out);
		archive.setEncoding("UTF-8");
		
		addAll(archive, baseDirFile.toURI(), baseDirFile);

		archive.finish();
		archive.flush();
		archive.close();

		out.flush();
		out.close();
		
	}
	
	/**
	 * add include file and directory
	 * @param archive
	 * @param baseUri
	 * @param targetFile
	 * @throws IOException
	 */
	private void addAll(ArchiveOutputStream archive, URI baseUri, File targetFile) throws IOException {
		if (targetFile.isDirectory()) {
			File[] children = targetFile.listFiles();

			if (children.length == 0) {
				addDir(archive, baseUri, targetFile);
			} else {
				for (File file : children) {
					addAll(archive, baseUri, file);
				}
			}

		} else {
			addFile(archive, baseUri, targetFile);
		}

	}
	
	/**
	 * add file to archive
	 * @param archive
	 * @param baseURI
	 * @param file
	 * @throws IOException
	 */
	private void addFile(ArchiveOutputStream archive, URI baseURI, File file) throws IOException {
		String name = PREFIX_DIR + baseURI.relativize(file.toURI()).getPath();
				
		archive.putArchiveEntry(archive.createArchiveEntry(file, name));
		IOUtils.copy(new FileInputStream(file), archive);
		archive.closeArchiveEntry();
	}

	/**
	 * add directory to archive
	 * @param archive
	 * @param baseURI
	 * @param file
	 * @throws IOException
	 */
	private void addDir(ArchiveOutputStream archive, URI baseURI, File file) throws IOException {
		String name = PREFIX_DIR + baseURI.relativize(file.toURI()).getPath();
		
		archive.putArchiveEntry(archive.createArchiveEntry(file, name));
		archive.closeArchiveEntry();
	}
}
