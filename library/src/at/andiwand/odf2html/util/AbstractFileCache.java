package at.andiwand.odf2html.util;

import java.io.File;
import java.net.URI;

import at.andiwand.odf2html.translator.File2URITranslator;

public abstract class AbstractFileCache implements FileCache {

    private File2URITranslator uriTranslator;

    public AbstractFileCache(File2URITranslator uriTranslator) {
	this.uriTranslator = uriTranslator;
    }

    public File2URITranslator getURITranslator() {
	return uriTranslator;
    }

    public URI getURI(String name) {
	File file = getFile(name);
	return uriTranslator.translate(file);
    }

    public void setURITranslator(File2URITranslator uriTranslator) {
	this.uriTranslator = uriTranslator;
    }

    public void deleteFile(File file) {
	delete(file.getName());
    }

}