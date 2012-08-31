package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import at.andiwand.odf2html.odf.OpenDocumentFile;


public class InlineImageTranslator extends ImageTranslator {
	
	private final OpenDocumentFile documentFile;
	
	public InlineImageTranslator(OpenDocumentFile documentFile) {
		this.documentFile = documentFile;
	}
	
	// TODO: optimize
	@Override
	public void writeSource(String path, Writer out) throws IOException {
		out.write("data:");
		out.write(documentFile.getFileMimetype(path));
		out.write(";base64,");
		
		InputStream in = documentFile.getFileStream(path);
		byte[] buffer = new byte[1024];
		for (int read; (read = in.read(buffer)) != -1;) {
			byte[] tmp = (read == buffer.length) ? buffer : Arrays.copyOf(
					buffer, read);
			out.write(Base64.encodeBase64String(tmp));
		}
	}
	
}