package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import at.andiwand.commons.codec.Base64OutputStream;
import at.andiwand.commons.io.ByteStreamUtil;
import at.andiwand.odf2html.odf.OpenDocumentFile;


public class InlineImageTranslator extends ImageTranslator {
	
	private final ByteStreamUtil byteStreamUtil = new ByteStreamUtil();
	
	public InlineImageTranslator(OpenDocumentFile documentFile) {
		super(documentFile);
	}
	
	@Override
	public void writeSource(String path, Writer out) throws IOException {
		out.write("data:");
		out.write(documentFile.getFileMimetype(path));
		out.write(";base64,");
		
		InputStream imgIn = documentFile.getFileStream(path);
		OutputStream imgOut = new Base64OutputStream(out);
		byteStreamUtil.writeStream(imgIn, imgOut);
	}
	
}