package at.andiwand.odf2html.translator.document;

import java.io.IOException;

import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.translator.settings.TranslationSettings;

public interface DocumentTranslator {

    public void translate(OpenDocument document, LWXMLWriter out,
	    TranslationSettings settings) throws IOException;

}