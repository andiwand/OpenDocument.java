package at.andiwand.odf2html.test;

import javax.swing.JFileChooser;


public class TestFileChooser extends JFileChooser {
	
	private static final long serialVersionUID = 1297640540776981863L;
	
	public TestFileChooser() {
		setCurrentDirectory(TestFile.getDirectory());
	}
	
}