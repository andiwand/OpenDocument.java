package at.andiwand.odf2html.translator;

import java.io.File;
import java.net.URI;


public interface File2URITranslator {
	
	public URI translate(File file);
	
}