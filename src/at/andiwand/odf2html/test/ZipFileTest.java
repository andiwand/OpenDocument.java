package at.andiwand.odf2html.test;

import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import at.andiwand.commons.util.EnumerationUtil;


public class ZipFileTest {
	
	public static void main(String[] args) throws Throwable {
		File file = new File("/home/andreas/encrypted.odt");
		ZipFile zipFile = new ZipFile(file);
		
		for (ZipEntry entry : EnumerationUtil.iterable(zipFile.entries())) {
			System.out.println(entry.getName());
		}
		
		zipFile.close();
	}
	
}