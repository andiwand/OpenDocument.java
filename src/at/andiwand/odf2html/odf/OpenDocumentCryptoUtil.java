package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.ZipException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.rtner.security.auth.spi.MacBasedPRF;
import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Parameters;


// TODO: improve class design
public class OpenDocumentCryptoUtil {
	
	public static boolean validatePassword(String password,
			OpenDocumentFile documentFile) throws IOException {
		Map<String, EncryptionParameter> encryptionParameterMap = documentFile
				.getEncryptionParameterMap();
		
		int minPlainSize = Integer.MAX_VALUE;
		String path = null;
		EncryptionParameter encryptionParameter = null;
		
		for (Map.Entry<String, EncryptionParameter> entry : encryptionParameterMap
				.entrySet()) {
			if (minPlainSize > entry.getValue().getPlainSize()) {
				minPlainSize = entry.getValue().getPlainSize();
				path = entry.getKey();
				encryptionParameter = entry.getValue();
				break;
			}
		}
		
		if (path == null) return true;
		InputStream inputStream = getPlainInputStream(documentFile
				.getRawFileStream(path), encryptionParameter, password);
		return validatePassword(password, encryptionParameter, inputStream);
	}
	
	public static boolean validatePassword(String password,
			EncryptionParameter encryptionParameter, InputStream in)
			throws IOException {
		try {
			String checksumAlgorithm = encryptionParameter.getChecksumType();
			byte[] checksum = encryptionParameter.getChecksum();
			
			String[] tmp = checksumAlgorithm.split("/");
			checksumAlgorithm = tmp[0];
			if (!tmp[1].equalsIgnoreCase("1K"))
				throw new UnsupportedEncryptionException(
						"unsupported checksum type: "
								+ encryptionParameter.getChecksumType());
			
			MessageDigest digest = MessageDigest.getInstance(checksumAlgorithm);
			byte[] bytes = new byte[1024];
			int count = in.read(bytes);
			digest.update(bytes, 0, count);
			
			if (!Arrays.equals(checksum, digest.digest())) return false;
		} catch (ZipException e) {
			return false;
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedEncryptionException(
					"unsupported message digest: "
							+ encryptionParameter.getChecksumType(), e);
		}
		
		return true;
	}
	
	public static InputStream getPlainInputStream(InputStream in,
			EncryptionParameter encryptionParameter, String password) {
		try {
			String keyDerivation = encryptionParameter.getKeyDerivation();
			if (!keyDerivation.equalsIgnoreCase("PBKDF2"))
				throw new UnsupportedEncryptionException(
						"unsupported key derivation: " + keyDerivation);
			
			String startKeyGeneration = encryptionParameter
					.getStartKeyGeneration();
			// TODO: password charset
			byte[] passwordBytes = password.getBytes();
			
			byte[] salt = encryptionParameter.getSalt();
			int iterationCount = encryptionParameter.getIterationCount();
			int keySize = encryptionParameter.getKeySize();
			String algorithm = encryptionParameter.getAlgorithm().toLowerCase();
			
			byte[] initialisationVector = encryptionParameter
					.getInitialisationVector();
			
			// TODO: improve
			String transformation;
			if (algorithm.contains("blowfish")) {
				algorithm = "Blowfish";
				transformation = "Blowfish/CFB/NoPadding";
			} else if (algorithm.contains("aes")) {
				algorithm = "AES";
				transformation = "AES/CBC/NoPadding";
			} else {
				throw new UnsupportedEncryptionException(
						"cannot identify algorithm: " + algorithm);
			}
			
			// ODF 1.0
			if (startKeyGeneration == null) startKeyGeneration = "SHA1";
			if (keySize == -1) keySize = 16;
			
			MessageDigest digest = MessageDigest
					.getInstance(startKeyGeneration);
			byte[] md = digest.digest(passwordBytes);
			
			MacBasedPRF macBasedPRF = new MacBasedPRF("HmacSHA1");
			PBKDF2Parameters pbkdf2Parameters = new PBKDF2Parameters(salt,
					iterationCount);
			PBKDF2Engine pbkdf2Engine = new PBKDF2Engine(pbkdf2Parameters,
					macBasedPRF);
			byte[] dk = pbkdf2Engine.deriveKey(md, keySize);
			Key key = new SecretKeySpec(dk, algorithm);
			
			IvParameterSpec iv = new IvParameterSpec(initialisationVector);
			
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			
			in = new CipherInputStream(in, cipher);
			
			return in;
		} catch (Exception e) {
			throw new UnsupportedEncryptionException(e);
		}
	}
	
	public OpenDocumentCryptoUtil() {}
	
}