package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.InputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;

import at.stefl.commons.codec.Base64;
import at.stefl.commons.codec.Base64Settings;
import at.stefl.commons.io.ByteStreamUtil;
import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import de.rtner.security.auth.spi.MacBasedPRF;
import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Parameters;

public class DecryptionTest {
    
    public static void main(String[] args) throws Throwable {
        JFileChooser fileChooser = new TestFileChooser();
        int option = fileChooser.showOpenDialog(null);
        
        if (option == JFileChooser.CANCEL_OPTION) return;
        
        File file = fileChooser.getSelectedFile();
        OpenDocumentFile documentFile = new LocatedOpenDocumentFile(file);
        
        InputStream inputStream = documentFile
                .getFileStream("Configurations2/accelerator/current.xml");
        
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] md = digest.digest("password".getBytes());
        
        byte[] salt = Base64.decodeChars("WAk+2ZssqTdcHFwpbDPQng==",
                Base64Settings.ORIGINAL);
        
        MacBasedPRF macBasedPRF = new MacBasedPRF("HmacSHA1");
        PBKDF2Parameters pbkdf2Parameters = new PBKDF2Parameters(salt, 1024);
        PBKDF2Engine pbkdf2Engine = new PBKDF2Engine(pbkdf2Parameters,
                macBasedPRF);
        byte[] dk = pbkdf2Engine.deriveKey(md, 16);
        Key key = new SecretKeySpec(dk, "Blowfish");
        
        IvParameterSpec iv = new IvParameterSpec(Base64.decodeChars(
                "xgVWDFN09qI=", Base64Settings.ORIGINAL));
        
        Cipher cipher = Cipher.getInstance("Blowfish/CFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        
        inputStream = new CipherInputStream(inputStream, cipher);
        inputStream = new InflaterInputStream(inputStream, new Inflater(true));
        
        ByteStreamUtil.writeStreamBytewise(inputStream, System.out);
        
        documentFile.close();
    }
    
}