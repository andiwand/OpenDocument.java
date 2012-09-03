package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.commons.codec.Base64;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;


public class EncryptionParameter {
	
	private static final String FILE_ENTRY_ELEMENT = "manifest:file-entry";
	private static final String ENCRYPTION_DATA_ELEMENT = "manifest:encryption-data";
	private static final String ALGORITHM_ELEMENT = "manifest:algorithm";
	private static final String KEY_DERIVATION_ELEMENT = "manifest:key-derivation";
	private static final String START_KEY_GENERATION_ELEMENT = "manifest:start-key-generation";
	
	private static final String FULL_PATH_ATTRIBUTE = "manifest:full-path";
	private static final String PLAIN_SIZE_ATTRIBUTE = "manifest:size";
	
	private static final String CHECKSUM_TYPE_ATTRIBUTE = "manifest:checksum-type";
	private static final String CHECKSUM_ATTRIBUTE = "manifest:checksum";
	
	private static final String ALGORITHM_ATTRIBUTE = "manifest:algorithm-name";
	private static final String INITIALISATION_VECTOR_ATTRIBUTE = "manifest:initialisation-vector";
	
	private static final String KEY_DERIVATION_ATTRIBUTE = "manifest:key-derivation-name";
	private static final String KEY_SIZE_ATTRIBUTE = "manifest:key-size";
	private static final String ITERATION_COUNT_ATTRIBUTE = "manifest:iteration-count";
	private static final String SALT_ATTRIBUTE = "manifest:salt";
	
	private static final String START_KEY_GENERATOR_ATTRIBUTE = "manifest:start-key-generation-name";
	private static final String START_KEY_SIZE_ATTRIBUTE = "manifest:key-size";
	
	// TODO: improve with path/query (0.0001% necessary)
	public static Map<String, EncryptionParameter> parseEncryptionParameters(
			OpenDocumentFile documentFile) throws IOException {
		Map<String, EncryptionParameter> result = new HashMap<String, EncryptionParameter>();
		LWXMLReader reader = new LWXMLStreamReader(documentFile.getManifest());
		
		String element = null;
		String file = null;
		EncryptionParameter encryptionParameter = new EncryptionParameter();
		
		while (true) {
			LWXMLEvent event = reader.readEvent();
			if (event == LWXMLEvent.END_DOCUMENT) break;
			
			switch (event) {
			case START_ELEMENT:
				element = reader.readValue();
				break;
			case END_ELEMENT:
				if (!reader.readValue().equals(FILE_ENTRY_ELEMENT)) continue;
				if (!encryptionParameter.isEmpty())
					result.put(file, encryptionParameter);
				encryptionParameter = new EncryptionParameter();
				break;
			case ATTRIBUTE_NAME:
				String attributeName = reader.readValue();
				String attributeValue = reader.readFollowingValue();
				if (element.equals(FILE_ENTRY_ELEMENT)
						&& attributeName.equals(FULL_PATH_ATTRIBUTE)) {
					file = attributeValue;
				} else {
					setEncryptionParameter(encryptionParameter, element,
							attributeName, attributeValue);
				}
				break;
			}
		}
		
		reader.close();
		return result;
	}
	
	private static void setEncryptionParameter(
			EncryptionParameter encryptionParameter, String node,
			String attributeName, String attributeValue) {
		if (node.equals(FILE_ENTRY_ELEMENT)) {
			if (attributeName.equals(PLAIN_SIZE_ATTRIBUTE)) {
				encryptionParameter.setPlainSize(Integer
						.parseInt(attributeValue));
			}
		} else if (node.equals(ENCRYPTION_DATA_ELEMENT)) {
			if (attributeName.equals(CHECKSUM_TYPE_ATTRIBUTE)) {
				encryptionParameter.setChecksumType(attributeValue);
			} else if (attributeName.equals(CHECKSUM_ATTRIBUTE)) {
				encryptionParameter.setChecksum(Base64
						.decodeChars(attributeValue));
			}
		} else if (node.equals(ALGORITHM_ELEMENT)) {
			if (attributeName.equals(ALGORITHM_ATTRIBUTE)) {
				encryptionParameter.setAlgorithm(attributeValue);
			} else if (attributeName.equals(INITIALISATION_VECTOR_ATTRIBUTE)) {
				encryptionParameter.setInitialisationVector(Base64
						.decodeChars(attributeValue));
			}
		} else if (node.equals(KEY_DERIVATION_ELEMENT)) {
			if (attributeName.equals(KEY_DERIVATION_ATTRIBUTE)) {
				encryptionParameter.setKeyDerivation(attributeValue);
			} else if (attributeName.equals(KEY_SIZE_ATTRIBUTE)) {
				encryptionParameter
						.setKeySize(Integer.parseInt(attributeValue));
			} else if (attributeName.equals(ITERATION_COUNT_ATTRIBUTE)) {
				encryptionParameter.setIterationCount(Integer
						.parseInt(attributeValue));
			} else if (attributeName.equals(SALT_ATTRIBUTE)) {
				encryptionParameter.setSalt(Base64.decodeChars(attributeValue));
			}
		} else if (node.equals(START_KEY_GENERATION_ELEMENT)) {
			if (attributeName.equals(START_KEY_GENERATOR_ATTRIBUTE)) {
				encryptionParameter.setStartKeyGeneration(attributeValue);
			} else if (attributeName.equals(START_KEY_SIZE_ATTRIBUTE)) {
				encryptionParameter.setStartKeySize(Integer
						.parseInt(attributeValue));
			}
		}
	}
	
	private int plainSize = -1;
	private String checksumType;
	private byte[] checksum;
	private String algorithm;
	private byte[] initialisationVector;
	private String keyDerivation;
	private int keySize = -1;
	private int iterationCount = -1;
	private byte[] salt;
	private String startKeyGeneration;
	private int startKeySize = -1;
	
	public boolean isEmpty() {
		if (plainSize != -1) return false;
		if (checksumType != null) return false;
		if (checksum != null) return false;
		if (algorithm != null) return false;
		if (initialisationVector != null) return false;
		if (keyDerivation != null) return false;
		if (keySize != -1) return false;
		if (iterationCount != -1) return false;
		if (salt != null) return false;
		if (startKeyGeneration != null) return false;
		if (startKeySize != -1) return false;
		
		return true;
	}
	
	public int getPlainSize() {
		return plainSize;
	}
	
	public void setPlainSize(int plainSize) {
		this.plainSize = plainSize;
	}
	
	public String getChecksumType() {
		return checksumType;
	}
	
	public void setChecksumType(String checksumType) {
		this.checksumType = checksumType;
	}
	
	public byte[] getChecksum() {
		return checksum;
	}
	
	public void setChecksum(byte[] checksum) {
		this.checksum = checksum;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	public byte[] getInitialisationVector() {
		return initialisationVector;
	}
	
	public void setInitialisationVector(byte[] initialisationVector) {
		this.initialisationVector = initialisationVector;
	}
	
	public String getKeyDerivation() {
		return keyDerivation;
	}
	
	public void setKeyDerivation(String keyDerivation) {
		this.keyDerivation = keyDerivation;
	}
	
	public int getKeySize() {
		return keySize;
	}
	
	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}
	
	public int getIterationCount() {
		return iterationCount;
	}
	
	public void setIterationCount(int iterationCount) {
		this.iterationCount = iterationCount;
	}
	
	public byte[] getSalt() {
		return salt;
	}
	
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
	public String getStartKeyGeneration() {
		return startKeyGeneration;
	}
	
	public void setStartKeyGeneration(String startKeyGeneration) {
		this.startKeyGeneration = startKeyGeneration;
	}
	
	public int getStartKeySize() {
		return startKeySize;
	}
	
	public void setStartKeySize(int startKeySize) {
		this.startKeySize = startKeySize;
	}
	
}