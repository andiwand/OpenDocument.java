package at.andiwand.odf2html.odf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import at.andiwand.commons.io.ByteStreamUtil;


public class TemporaryOpenDocumentFile extends LocatedOpenDocumentFile {
	
	private static final String PREFIX = "ooreadertmp";
	
	public TemporaryOpenDocumentFile(File file) throws IOException {
		super(file);
	}
	
	public TemporaryOpenDocumentFile(InputStream inputStream)
			throws IOException {
		super(File.createTempFile(PREFIX, ""));
		FileOutputStream outputStream = new FileOutputStream(getFile());
		ByteStreamUtil.writeStreamBuffered(inputStream, outputStream);
		outputStream.close();
	}
	
}