package at.stefl.opendocument.java.odf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.stefl.commons.io.ByteStreamUtil;
import at.stefl.opendocument.java.util.FileCache;

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
	File file = fileCache.create(name);

	OutputStream outputStream = fileCache.getOutputStream(name);
	ByteStreamUtil.writeStreamBuffered(inputStream, outputStream);
	outputStream.close();

	init(file);
    }

}