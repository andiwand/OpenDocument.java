package at.stefl.opendocument.java.test;

import javax.swing.JFileChooser;

public class TestFileChooser extends JFileChooser {

    private static final long serialVersionUID = 1297640540776981863L;

    public TestFileChooser() {
	this("");
    }

    public TestFileChooser(String child) {
	setCurrentDirectory(TestFileUtil.getFile(child));
    }

}