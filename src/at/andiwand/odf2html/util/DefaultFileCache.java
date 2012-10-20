package at.andiwand.odf2html.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.odf2html.translator.File2URITranslator;


public class DefaultFileCache extends AbstractFileCache {
	
	private final File directory;
	
	public DefaultFileCache(String directory) {
		this(new File(directory));
	}
	
	public DefaultFileCache(File directory) {
		super(File2URITranslator.DEFAULT);
		
		if (!directory.exists() && !directory.mkdir())
			throw new IllegalStateException();
		if (!directory.isDirectory()) throw new IllegalArgumentException();
		this.directory = directory;
	}
	
	@Override
	public boolean isFile(String name) {
		return getFile(name).exists();
	}
	
	@Override
	public File getFile(String name) {
		return new File(directory, name);
	}
	
	@Override
	public InputStream getFileInputStream(String name)
			throws FileNotFoundException {
		File file = getFile(name);
		return new FileInputStream(file);
	}
	
	@Override
	public OutputStream getFileOutputStream(String name)
			throws FileNotFoundException {
		File file = getFile(name);
		return new FileOutputStream(file);
	}
	
	@Override
	public File newFile(String name) {
		return getFile(name);
	}
	
	@Override
	public void deleteFile(String name) {
		File file = getFile(name);
		file.delete();
	}
	
	@Override
	public void clear() {
		directory.delete();
	}
	
}