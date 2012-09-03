package at.andiwand.odf2html.test;

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

import at.andiwand.common.codec.Base64;
import at.andiwand.common.io.ByteStreamUtil;
import at.andiwand.odf2html.odf.TemporaryOpenDocumentFile;
import de.rtner.security.auth.spi.MacBasedPRF;
import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Parameters;


public class DecryptionTest {
	
	public static void main(String[] args) throws Throwable {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(null);
		
		if (option == JFileChooser.CANCEL_OPTION) return;
		
		File file = fileChooser.getSelectedFile();
		TemporaryOpenDocumentFile documentFile = new TemporaryOpenDocumentFile(
				file);
		
		InputStream inputStream = documentFile.getFileStream("meta.xml");
		
		MessageDigest digest = MessageDigest.getInstance("SHA1");
		byte[] md = digest.digest("password".getBytes());
		
		byte[] salt = Base64.decodeChars("WAk+2ZssqTdcHFwpbDPQng==");
		
		MacBasedPRF macBasedPRF = new MacBasedPRF("HmacSHA1");
		PBKDF2Parameters pbkdf2Parameters = new PBKDF2Parameters(salt, 1024);
		PBKDF2Engine pbkdf2Engine = new PBKDF2Engine(pbkdf2Parameters,
				macBasedPRF);
		byte[] dk = pbkdf2Engine.deriveKey(md, 16);
		Key key = new SecretKeySpec(dk, "Blowfish");
		
		IvParameterSpec iv = new IvParameterSpec(Base64
				.decodeChars("xgVWDFN09qI="));
		
		Cipher cipher = Cipher.getInstance("Blowfish/CFB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		
		inputStream = new CipherInputStream(inputStream, cipher);
		inputStream = new InflaterInputStream(inputStream, new Inflater(true));
		
		ByteStreamUtil.writeStreamBytewise(inputStream, System.out);
	}
	
}