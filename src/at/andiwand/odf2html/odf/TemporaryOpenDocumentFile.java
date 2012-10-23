package at.andiwand.odf2html.odf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.commons.io.ByteStreamUtil;
import at.andiwand.odf2html.util.FileCache;


public class TemporaryOpenDocumentFile extends LocatedOpenDocumentFile {
	
	private static int lastIdentity;
	
	public TemporaryOpenDocumentFile(File file) throws IOException {
		super(file);
	}
	
	// TODO: improve
	public TemporaryOpenDocumentFile(InputStream inputStream,
			FileCache fileCache) throws IOException {
		// TODO: fix ugly
		String name = "odf-" + (lastIdentity++) + ".odt";
		File file = fileCache.newFile(name);
		
		OutputStream outputStream = fileCache.getFileOutputStream(name);
		ByteStreamUtil.writeStreamBuffered(inputStream, outputStream);
		outputStream.close();
		
		init(file);
	}
	
}