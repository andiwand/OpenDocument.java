package at.andiwand.odf2html.translator;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;


public class FileCache {
	
	private boolean autoClean;
	
	private File2URITranslator uriTranslator;
	
	private final File directory;
	private Set<File> cachedFiles;
	
	public FileCache(String directory) {
		this(new File(directory), true);
	}
	
	public FileCache(File directory) {
		this(directory, true);
	}
	
	public FileCache(String directory, boolean autoClean) {
		this(new File(directory), autoClean,
				File2URITranslator.DEFAULT_INSTANCE);
	}
	
	public FileCache(File directory, boolean autoClean) {
		this(directory, autoClean, File2URITranslator.DEFAULT_INSTANCE);
	}
	
	public FileCache(String directory, boolean autoClean,
			File2URITranslator uriTranslator) {
		this(new File(directory), autoClean, uriTranslator);
	}
	
	public FileCache(File directory, boolean autoClean,
			File2URITranslator uriTranslator) {
		this.autoClean = autoClean;
		this.uriTranslator = uriTranslator;
		this.directory = directory;
		this.cachedFiles = new HashSet<File>();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (!FileCache.this.autoClean) return;
				
				clean();
			}
		});
	}
	
	public File getCacheDirectory() {
		return directory;
	}
	
	public boolean isAutoClean() {
		return autoClean;
	}
	
	public boolean isFileCached(String fileName) {
		File file = new File(directory, fileName);
		return file.isFile();
	}
	
	public File getCachedFile(String fileName) {
		if (!isFileCached(fileName)) return null;
		return new File(directory, fileName);
	}
	
	public URI getCachedFileURI(String fileName) {
		File file = getCachedFile(fileName);
		if (file == null) return null;
		return uriTranslator.translate(file);
	}
	
	public void setAutoClean(boolean autoClean) {
		this.autoClean = autoClean;
	}
	
	public File getNewCachedFile(String fileName) {
		File file = new File(directory, fileName);
		if (file.isFile()) file.delete();
		
		cachedFiles.add(file);
		
		return file;
	}
	
	public void clean() {
		for (File file : cachedFiles) {
			file.delete();
		}
		
		cachedFiles.clear();
	}
	
}