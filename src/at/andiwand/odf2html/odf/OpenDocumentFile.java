package at.andiwand.odf2html.odf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import at.andiwand.common.io.CharacterStreamUtil;
import at.andiwand.common.io.FluidInputStreamReader;
import at.andiwand.common.lwxml.LWXMLEvent;
import at.andiwand.common.lwxml.reader.LWXMLReader;
import at.andiwand.common.lwxml.reader.LWXMLReaderException;
import at.andiwand.common.lwxml.reader.LWXMLStreamReader;
import at.andiwand.common.util.ArrayUtil;


public abstract class OpenDocumentFile {
	
	private static final String MIMETYPE_PATH = "mimetype";
	private static final String MANIFEST_PATH = "META-INF/manifest.xml";
	
	private static final Set<String> UNENCRYPTED_FILES = ArrayUtil
			.toHashSet(new String[] {MIMETYPE_PATH, MANIFEST_PATH});
	
	private String mimetype;
	
	private Map<String, EncryptionParameter> encryptionParameterMap;
	private String password;
	
	public boolean isEncrypted() throws IOException {
		if (encryptionParameterMap == null) {
			try {
				encryptionParameterMap = Collections
						.unmodifiableMap(EncryptionParameter
								.parseEncryptionParameters(this));
			} catch (LWXMLReaderException e) {
				throw new IllegalStateException("lwxml exception", e);
			}
		}
		
		return !encryptionParameterMap.isEmpty();
	}
	
	public boolean isFileEncrypted(String path) throws IOException {
		if (UNENCRYPTED_FILES.contains(path)) return false;
		if (!isEncrypted()) return false;
		
		return encryptionParameterMap.containsKey(path);
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isPasswordValid(String password) throws IOException {
		if (!isEncrypted()) return true;
		
		return OpenDocumentCryptoUtil.validatePassword(password, this);
	}
	
	public EncryptionParameter getEncryptionParameter(String path)
			throws IOException {
		if (!isEncrypted()) return null;
		
		return encryptionParameterMap.get(path);
	}
	
	public Map<String, EncryptionParameter> getEncryptionParameterMap()
			throws IOException {
		if (!isEncrypted()) return null;
		
		return encryptionParameterMap;
	}
	
	public abstract List<String> getFileList() throws IOException;
	
	protected abstract InputStream getRawFileStream(String path)
			throws IOException;
	
	public InputStream getFileStream(String path) throws IOException {
		InputStream inputStream = getRawFileStream(path);
		if (!isFileEncrypted(path)) return inputStream;
		
		if (password == null)
			throw new NullPointerException("password cannot be null");
		EncryptionParameter encryptionParameter = getEncryptionParameter(path);
		inputStream = OpenDocumentCryptoUtil.getPlainInputStream(inputStream,
				encryptionParameter, password);
		inputStream = new InflaterInputStream(inputStream, new Inflater(true));
		return inputStream;
	}
	
	// TODO: improve
	public String getFileMimetype(String path) throws IOException {
		LWXMLReader in = new LWXMLStreamReader(getManifest());
		
		String mimetype = null;
		String fullPath = null;
		
		while (true) {
			LWXMLEvent event = in.readEvent();
			if (event == LWXMLEvent.END_DOCUMENT) break;
			
			switch (event) {
			case ATTRIBUTE_NAME:
				String attributeName = in.readValue();
				
				if (attributeName.equals("manifest:media-type")) {
					mimetype = in.readFollowingValue();
				} else if (attributeName.equals("manifest:full-path")) {
					fullPath = in.readFollowingValue();
				}
				
				break;
			case END_ATTRIBUTE_LIST:
				if ((mimetype != null) && (path.equals(fullPath)))
					return mimetype;
				
				mimetype = null;
				fullPath = null;
				break;
			}
		}
		
		return null;
	}
	
	public String getMimetype() throws IOException {
		if (mimetype == null) {
			InputStream in = getRawFileStream(MIMETYPE_PATH);
			if (in == null)
				throw new IllegalStateException("mimetype file does not exist");
			
			mimetype = CharacterStreamUtil
					.readAsString(new FluidInputStreamReader(in));
		}
		
		return mimetype;
	}
	
	public InputStream getManifest() throws IOException {
		return getRawFileStream(MANIFEST_PATH);
	}
	
	public OpenDocument getAsOpenDocument() throws IOException {
		String mimetype = getMimetype();
		
		if (OpenDocumentText.checkMimetype(mimetype)) {
			return getAsOpenDocumentText();
		} else if (OpenDocumentSpreadsheet.checkMimetype(mimetype)) {
			return getAsOpenDocumentSpreadsheet();
		} else if (OpenDocumentPresentation.checkMimetype(mimetype)) {
			return getAsOpenDocumentPresentation();
		}
		
		throw new IllegalStateException("unsupported mimetype");
	}
	
	public OpenDocumentText getAsOpenDocumentText() throws IOException {
		return new OpenDocumentText(this);
	}
	
	public OpenDocumentSpreadsheet getAsOpenDocumentSpreadsheet()
			throws IOException {
		return new OpenDocumentSpreadsheet(this);
	}
	
	public OpenDocumentPresentation getAsOpenDocumentPresentation()
			throws IOException {
		return new OpenDocumentPresentation(this);
	}
	
}