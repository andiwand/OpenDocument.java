package at.stefl.opendocument.java.test;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class TestFileChooser extends JFileChooser {
    
    private static final long serialVersionUID = 1297640540776981863L;
    
    private static final FileFilter FILE_FILTER = new FileFilter() {
        @Override
        public String getDescription() {
            return "OpenDocument Test Files";
        }
        
        @Override
        public boolean accept(File f) {
            return TestFile.fromPattern(f) != null;
        }
    };
    
    public TestFileChooser() {
        this("");
    }
    
    public TestFileChooser(String child) {
        setCurrentDirectory(TestFileUtil.getFile(child));
        setFileFilter(FILE_FILTER);
    }
    
    public TestFile getSelectedTestFile() {
        return TestFile.fromPattern(getSelectedFile());
    }
    
}