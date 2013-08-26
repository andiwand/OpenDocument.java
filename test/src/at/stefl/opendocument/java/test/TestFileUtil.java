package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TestFileUtil {
    
    private static final Preferences PREFERENCES = Preferences
            .userNodeForPackage(TestFileUtil.class);
    private static final String TEST_DIRECTORY_KEY = "testDirectory";
    
    private static final File TEST_DIRECTORY;
    
    static {
        String testDirectory = PREFERENCES.get("testDirectory", null);
        
        if (testDirectory == null) {
            JOptionPane.showMessageDialog(null, "No test directory set!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) throw new IllegalStateException(
                    "you need to select a test directory to continue");
            
            testDirectory = chooser.getSelectedFile().getAbsolutePath();
            PREFERENCES.put(TEST_DIRECTORY_KEY, testDirectory);
        }
        
        TEST_DIRECTORY = new File(testDirectory);
        
        try {
            PREFERENCES.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
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
    
    public static Set<TestFile> getFiles() {
        Set<TestFile> result = new HashSet<TestFile>();
        getFilesImpl(getDirectory(), result);
        return result;
    }
    
    private static void getFilesImpl(File directory, Set<TestFile> result) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                getFilesImpl(file, result);
            } else {
                TestFile testFile = TestFile.fromPattern(file);
                if (testFile != null) result.add(testFile);
            }
        }
    }
    
    private TestFileUtil() {}
    
}