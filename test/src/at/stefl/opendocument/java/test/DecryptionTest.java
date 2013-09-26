package at.stefl.opendocument.java.test;

import java.io.File;

import javax.swing.JFileChooser;

import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentFile;

public class DecryptionTest {
    
    public static void main(String[] args) throws Throwable {
        JFileChooser fileChooser = new TestFileChooser();
        int option = fileChooser.showOpenDialog(null);
        
        if (option == JFileChooser.CANCEL_OPTION) return;
        
        File file = fileChooser.getSelectedFile();
        OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
        
        documentFile.setPassword("asdf");
        System.out.println(documentFile.isPasswordValid());
        
        documentFile.close();
    }
    
}