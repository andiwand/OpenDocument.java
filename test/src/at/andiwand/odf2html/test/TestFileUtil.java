package at.andiwand.odf2html.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import at.andiwand.commons.util.InaccessibleSectionException;


public class TestFileUtil {
	
	private static final ClassLoader CLASS_LOADER = Thread.currentThread()
			.getContextClassLoader();
	private static final URL PROGRAM_URL = CLASS_LOADER.getResource(".");
	private static final File PROGRAM_DIRECTORY;
	
	private static final String RELATIVE_TEST_DIRECTORY = "../dep/OpenDocument.test/files";
	private static final File TEST_DIRECTORY;
	
	static {
		try {
			PROGRAM_DIRECTORY = new File(PROGRAM_URL.toURI());
			TEST_DIRECTORY = new File(PROGRAM_DIRECTORY,
					RELATIVE_TEST_DIRECTORY).getCanonicalFile();
		} catch (Exception e) {
			throw new InaccessibleSectionException();
		}
	}
	
	public static File getDirectory() {
		return TEST_DIRECTORY;
	}
	
	public static File getFile(String name) {
		return new File(getDirectory(), name);
	}
	
	public static InputStream getInputStream(String name)
			throws FileNotFoundException {
		return new FileInputStream(new File(TEST_DIRECTORY, name));
	}
	
	private TestFileUtil() {}
	
}