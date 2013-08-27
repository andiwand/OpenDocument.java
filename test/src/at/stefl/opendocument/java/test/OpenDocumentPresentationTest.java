package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentPresentation;

public class OpenDocumentPresentationTest {
    
    public static void test(OpenDocumentPresentation presentation)
            throws IOException {
        presentation.getPageCount();
        presentation.getPageNames();
    }
    
    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new TestFileChooser();
        int option = fileChooser.showOpenDialog(null);
        
        if (option == JFileChooser.CANCEL_OPTION) return;
        
        File file = fileChooser.getSelectedFile();
        OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
        OpenDocumentPresentation presentation = documentFile.getAsDocument()
                .getAsPresentation();
        
        test(presentation);
        
        documentFile.close();
    }
    
}
