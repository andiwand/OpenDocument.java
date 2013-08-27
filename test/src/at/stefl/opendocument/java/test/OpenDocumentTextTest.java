package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentText;

public class OpenDocumentTextTest {
    
    public static void test(OpenDocumentText text) throws IOException {
        text.getPageCount();
        text.isSoftPageBreaks();
    }
    
    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new TestFileChooser();
        int option = fileChooser.showOpenDialog(null);
        
        if (option == JFileChooser.CANCEL_OPTION) return;
        
        File file = fileChooser.getSelectedFile();
        OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
        OpenDocumentText text = documentFile.getAsDocument().getAsText();
        
        test(text);
        
        documentFile.close();
    }
    
}