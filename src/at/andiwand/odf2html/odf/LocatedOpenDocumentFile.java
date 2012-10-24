package at.andiwand.odf2html.odf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import at.andiwand.commons.util.EnumerationUtil;


public class LocatedOpenDocumentFile extends OpenDocumentFile {
	
	private File file;
	private ZipFile zipFile;
	
	private Map<String, ZipEntry> entryMap;
	
	// TODO: improve
	protected LocatedOpenDocumentFile() {}
	
	public LocatedOpenDocumentFile(File file) throws IOException {
		init(file);
	}
	
	// TODO: improve
	protected void init(File file) throws IOException {
		this.file = file;
		this.zipFile = new ZipFile(file);
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public boolean isFile(String name) throws IOException {
		if (entryMap == null) getFileNames();
		return entryMap.containsKey(name);
	}
	
	@Override
	public Set<String> getFileNames() throws IOException {
		if (entryMap == null) {
			entryMap = new HashMap<String, ZipEntry>();
			
			for (ZipEntry entry : EnumerationUtil.iterable(zipFile.entries())) {
				entryMap.put(entry.getName(), entry);
			}
		}
		
		return entryMap.keySet();
	}
	
	@Override
	protected InputStream getRawFileStream(String name) throws IOException {
		if (entryMap == null) getFileNames();
		
		ZipEntry entry = entryMap.get(name);
		if (entry == null)
			throw new FileNotFoundException("file does not exist: " + name);
		
		return zipFile.getInputStream(entry);
	}
	
}