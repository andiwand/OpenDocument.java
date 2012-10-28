package at.andiwand.odf2html.test;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;


public class TestFile {
	
	private static final String PACKAGE = "files";
	
	public static File getDirectory() {
		try {
			URL url = TestFile.class.getResource(PACKAGE);
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static File getFile(String name) {
		return new File(getDirectory(), name);
	}
	
	public static InputStream getInputStream(String name) {
		return TestFile.class.getResourceAsStream(PACKAGE + "/" + name);
	}
	
	private TestFile() {}
	
}