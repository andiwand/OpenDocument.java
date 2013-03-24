package at.andiwand.odf2html.translator.content;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import at.andiwand.commons.io.ByteStreamUtil;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.util.FileCache;

public class CachedImageTranslator extends ImageTranslator {

	private final ByteStreamUtil streamUtil = new ByteStreamUtil();

	private final FileCache fileCache;

	public CachedImageTranslator(OpenDocumentFile documentFile,
			FileCache fileCache) {
		super(documentFile);

		this.fileCache = fileCache;
	}

	@Override
	public void writeSource(String name, Writer out) throws IOException {
		String imageName = new File(name).getName();

		if (!fileCache.isFile(imageName)) {
			File file = fileCache.newFile(imageName);
			InputStream fileIn = documentFile.getFileStream(name);
			OutputStream fileOut = new FileOutputStream(file);

			try {
				streamUtil.writeStream(fileIn, fileOut);
			} finally {
				fileOut.close();
				fileIn.close();
			}
		}

		out.write(fileCache.getFileURI(imageName).toString());
	}

}