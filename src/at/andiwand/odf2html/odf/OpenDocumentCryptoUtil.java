package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.zip.ZipException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import at.andiwand.commons.util.collection.CollectionUtil;
import at.andiwand.commons.util.comparator.MapEntryValueComparator;
import de.rtner.security.auth.spi.MacBasedPRF;
import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Parameters;


// TODO: improve class design
public class OpenDocumentCryptoUtil {
	
	private static final Comparator<EncryptionParameter> PLAIN_SIZE_COMPERATOR = new Comparator<EncryptionParameter>() {
		@Override
		public int compare(EncryptionParameter o1, EncryptionParameter o2) {
			return o1.getPlainSize() - o2.getPlainSize();
		}
	};
	
	private static final MapEntryValueComparator<EncryptionParameter> ENTRY_PLAIN_SIZE_COMPERATOR = new MapEntryValueComparator<EncryptionParameter>(
			PLAIN_SIZE_COMPERATOR);
	
	public static boolean validatePassword(String password,
			OpenDocumentFile documentFile) throws IOException {
		Map<String, EncryptionParameter> encryptionParameterMap = documentFile
				.getEncryptionParameterMap();
		
		Map.Entry<String, EncryptionParameter> smallest = CollectionUtil
				.getSmallest(ENTRY_PLAIN_SIZE_COMPERATOR,
						encryptionParameterMap.entrySet());
		
		if (smallest == null) return true;
		String path = smallest.getKey();
		EncryptionParameter encryptionParameter = smallest.getValue();
		InputStream in = getPlainInputStream(documentFile
				.getRawFileStream(path), encryptionParameter, password);
		return validatePassword(password, encryptionParameter, in);
	}
	
	public static boolean validatePassword(String password,
			EncryptionParameter encryptionParameter, InputStream in)
			throws IOException {
		try {
			String checksumAlgorithm = encryptionParameter.getChecksumType()
					.toLowerCase();
			byte[] checksum = encryptionParameter.getChecksum();
			
			if (!checksumAlgorithm.contains("1k"))
				throw new UnsupportedEncryptionException(
						"unsupported checksum: "
								+ encryptionParameter.getChecksumType());
			if (checksumAlgorithm.contains("sha256")) {
				checksumAlgorithm = "SHA-256";
			} else if (checksumAlgorithm.contains("sha1")) {
				checksumAlgorithm = "SHA-1";
			} else {
				throw new UnsupportedEncryptionException(
						"cannot identify checksum algorithm: "
								+ checksumAlgorithm);
			}
			
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
		String keyDerivation = encryptionParameter.getKeyDerivation();
		if (!keyDerivation.equalsIgnoreCase("PBKDF2"))
			throw new UnsupportedEncryptionException(
					"unsupported key derivation: " + keyDerivation);
		
		String startKeyGeneration = encryptionParameter.getStartKeyGeneration()
				.toLowerCase();
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
					"cannot identify crypto algorithm: " + algorithm);
		}
		
		// odf 1.0
		if (keySize == -1) keySize = 16;
		if (startKeyGeneration == null) {
			startKeyGeneration = "SHA-1";
		} else {
			if (startKeyGeneration.contains("sha256")) {
				startKeyGeneration = "SHA-256";
			} else if (startKeyGeneration.contains("sha1")) {
				startKeyGeneration = "SHA-1";
			} else {
				throw new UnsupportedEncryptionException(
						"cannot identify mac algorithm: " + startKeyGeneration);
			}
		}
		
		try {
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