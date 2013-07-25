package at.stefl.opendocument.java.translator.document;

import java.io.IOException;

import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.translator.settings.TranslationSettings;

public interface DocumentTranslator {

    public void translate(OpenDocument document, LWXMLWriter out,
	    TranslationSettings settings) throws IOException;

}