package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipInputStreamTest {
	
	public static void main(String[] args) throws Throwable {
		File file = TestFileUtil.getFile("encrypted.odt");
		InputStream in = new FileInputStream(file);
		ZipInputStream zin = new ZipInputStream(in);
		
		while (true) {
			ZipEntry entry = zin.getNextEntry();
			if (entry == null) break;
			System.out.println(entry.getName());
		}
		
		zin.close();
	}
	
}