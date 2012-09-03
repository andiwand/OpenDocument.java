package at.andiwand.odf2html.translator.content;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import at.andiwand.commons.io.ByteStreamUtil;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.FileCache;


public class CachedImageTranslator extends ImageTranslator {
	
	private final ByteStreamUtil streamUtil = new ByteStreamUtil();
	
	private final OpenDocumentFile documentFile;
	private final FileCache fileCache;
	
	public CachedImageTranslator(OpenDocumentFile documentFile,
			FileCache fileCache) {
		this.documentFile = documentFile;
		this.fileCache = fileCache;
	}
	
	@Override
	public void writeSource(String path, Writer out) throws IOException {
		String imageName = new File(path).getName();
		
		if (!fileCache.isFileCached(imageName)) {
			File file = fileCache.getNewCachedFile(imageName);
			InputStream fileIn = documentFile.getFileStream(path);
			OutputStream fileOut = new FileOutputStream(file);
			streamUtil.writeStream(fileIn, fileOut);
		}
		
		out.write(fileCache.getCachedFileURI(imageName).toString());
	}
	
}