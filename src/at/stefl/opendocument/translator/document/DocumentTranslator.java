package at.stefl.opendocument.translator.document;

import java.io.IOException;

import at.stefl.commons.lwxml.writer.LWXMLWriter;
import at.stefl.opendocument.odf.OpenDocument;
import at.stefl.opendocument.translator.settings.TranslationSettings;

public interface DocumentTranslator {

    public void translate(OpenDocument document, LWXMLWriter out,
	    TranslationSettings settings) throws IOException;

}