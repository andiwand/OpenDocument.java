package at.andiwand.odf2html.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;


public interface FileCache {
	
	public abstract boolean exists(String name);
	
	public abstract File getFile(String name);
	
	public abstract RandomAccessFile getRandomAccessFile(String name,
			String mode) throws FileNotFoundException;
	
	public abstract URI getURI(String name);
	
	public abstract InputStream getInputStream(String name)
			throws FileNotFoundException;
	
	public abstract OutputStream getOutputStream(String name)
			throws FileNotFoundException;
	
	public abstract FileChannel getChannel(String name, String mode)
			throws FileNotFoundException;
	
	public abstract File create(String name);
	
	public abstract void delete(String name);
	
	public abstract void clear();
	
}