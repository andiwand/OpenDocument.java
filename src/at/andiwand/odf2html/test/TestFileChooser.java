package at.andiwand.odf2html.test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFileChooser;


public class TestFileChooser extends JFileChooser {
	
	private static final long serialVersionUID = 1297640540776981863L;
	
	public TestFileChooser() {
		try {
			URL url = TestFileChooser.class.getResource("files");
			File directory = new File(url.toURI());
			setCurrentDirectory(directory);
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
	
}