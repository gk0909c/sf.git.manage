package gk0909c.sf.git.manage.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.utils.IOUtils;

public class ZipHelper {
	/**
	 * make zip file
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
		archive.setEncoding("Windows-31J");
		
		String basePath = baseDirFile.getAbsolutePath();
		addAll(archive, basePath, baseDirFile);

		archive.finish();
		archive.flush();
		archive.close();

		out.flush();
		
	}
	
	/**
	 * add include file and directory
	 * @param archive
	 * @param basePath
	 * @param targetFile
	 * @throws IOException
	 */
	private void addAll(ArchiveOutputStream archive, String basePath, File targetFile) throws IOException {
		if (targetFile.isDirectory()) {
			File[] children = targetFile.listFiles();

			if (children.length == 0) {
				addDir(archive, basePath, targetFile);
			} else {
				for (File file : children) {
					addAll(archive, basePath, file);
				}
			}

		} else {
			addFile(archive, basePath, targetFile);
		}

	}
	
	/**
	 * add file to archive
	 * @param archive
	 * @param basePath
	 * @param file
	 * @throws IOException
	 */
	private void addFile(ArchiveOutputStream archive, String basePath, File file) throws IOException {
		String path = file.getAbsolutePath();
		String name = path.substring(basePath.length());

		archive.putArchiveEntry(new ZipArchiveEntry(name));
		IOUtils.copy(new FileInputStream(file), archive, 8192);
		archive.closeArchiveEntry();
	}

	/**
	 * add directory to archive
	 * @param archive
	 * @param basePath
	 * @param file
	 * @throws IOException
	 */
	private void addDir(ArchiveOutputStream archive, String basePath, File file) throws IOException {
		String path = file.getAbsolutePath();
		String name = path.substring(basePath.length());

		archive.putArchiveEntry(new ZipArchiveEntry(name + "/"));
		archive.closeArchiveEntry();
	}
}
