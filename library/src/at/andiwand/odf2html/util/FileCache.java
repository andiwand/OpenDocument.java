package at.andiwand.odf2html.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;


public interface FileCache {
	
	public abstract boolean isFile(String name);
	
	public abstract File getFile(String name);
	
	public abstract URI getFileURI(String name);
	
	public abstract InputStream getFileInputStream(String name)
			throws FileNotFoundException;
	
	public abstract OutputStream getFileOutputStream(String name)
			throws FileNotFoundException;
	
	public abstract File newFile(String name);
	
	public abstract void deleteFile(String name);
	
	public abstract void clear();
	
}