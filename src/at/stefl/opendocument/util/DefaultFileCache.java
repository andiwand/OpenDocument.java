package at.stefl.opendocument.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import at.stefl.opendocument.translator.File2URITranslator;

public class DefaultFileCache extends AbstractFileCache {

    private final File directory;

    public DefaultFileCache(String directory) {
	this(new File(directory), File2URITranslator.DEFAULT);
    }

    public DefaultFileCache(String directory, File2URITranslator uriTranslator) {
	this(new File(directory), uriTranslator);
    }

    public DefaultFileCache(File directory, File2URITranslator uriTranslator) {
	super(uriTranslator);

	if (!directory.exists() && !directory.mkdir())
	    throw new IllegalStateException();
	if (!directory.isDirectory())
	    throw new IllegalArgumentException();
	this.directory = directory;
    }

    public File getDirectory() {
	return directory;
    }

    @Override
    public boolean exists(String name) {
	return getFile(name).exists();
    }

    @Override
    public File getFile(String name) {
	return new File(directory, name);
    }

    @Override
    public RandomAccessFile getRandomAccessFile(String name, String mode)
	    throws FileNotFoundException {
	return new RandomAccessFile(getFile(name), mode);
    }

    @Override
    public InputStream getInputStream(String name) throws FileNotFoundException {
	return new FileInputStream(getFile(name));
    }

    @Override
    public OutputStream getOutputStream(String name)
	    throws FileNotFoundException {
	return new FileOutputStream(getFile(name));
    }

    @Override
    public FileChannel getChannel(String name, String mode)
	    throws FileNotFoundException {
	return getRandomAccessFile(name, mode).getChannel();
    }

    @Override
    public File create(String name) {
	return getFile(name);
    }

    @Override
    public void delete(String name) {
	getFile(name).delete();
    }

    @Override
    public void clear() {
	directory.delete();
    }

}