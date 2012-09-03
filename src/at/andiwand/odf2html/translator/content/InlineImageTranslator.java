package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import at.andiwand.common.codec.Base64OutputStream;
import at.andiwand.common.io.ByteStreamUtil;
import at.andiwand.odf2html.odf.OpenDocumentFile;


public class InlineImageTranslator extends ImageTranslator {
	
	private final ByteStreamUtil byteStreamUtil = new ByteStreamUtil();
	
	private final OpenDocumentFile documentFile;
	
	public InlineImageTranslator(OpenDocumentFile documentFile) {
		this.documentFile = documentFile;
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