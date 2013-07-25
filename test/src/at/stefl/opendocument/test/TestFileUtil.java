package at.stefl.opendocument.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import at.stefl.commons.util.InaccessibleSectionException;

public class TestFileUtil {

    private static final ClassLoader CLASS_LOADER = Thread.currentThread()
	    .getContextClassLoader();
    private static final URL PROGRAM_URL = CLASS_LOADER.getResource(".");
    private static final File PROGRAM_DIRECTORY;

    private static final String RELATIVE_TEST_DIRECTORY = "../dep/OpenDocument.test/files";
    private static final File TEST_DIRECTORY;

    private static final String PASSWORD_SEPARATOR_PATTERN = "\\$";

    private static final String IGNORE_PREFIX = "#";

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

    public static String getPassword(String name) {
	String[] split = name.split(PASSWORD_SEPARATOR_PATTERN);
	return (split.length == 3) ? split[1] : null;
    }

    public static boolean isFileIgnored(String name) {
	return name.startsWith(IGNORE_PREFIX);
    }

    public static Map<File, String> getFiles() {
	Map<File, String> result = new HashMap<File, String>();
	getFilesImpl(getDirectory(), result);
	return result;
    }

    private static void getFilesImpl(File parent, Map<File, String> result) {
	for (File child : parent.listFiles()) {
	    if (isFileIgnored(child.getName()))
		continue;

	    if (child.isDirectory())
		getFilesImpl(child, result);
	    else
		result.put(child, getPassword(child.getName()));
	}
    }

    private TestFileUtil() {
    }

}