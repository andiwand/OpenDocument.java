package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.commons.lwxml.translator.LWXMLElementReplacement;
import at.andiwand.odf2html.translator.ScriptGenerator;
import at.andiwand.odf2html.translator.StyleGenerator;
import at.andiwand.odf2html.translator.context.TranslationContext;

public class DefaultElementTranslator extends
	LWXMLElementReplacement<TranslationContext> implements
	StyleGenerator<TranslationContext>, ScriptGenerator<TranslationContext> {

    public DefaultElementTranslator(String elementName) {
	super(elementName);
    }

    @Override
    public void generateStyle(Writer out, TranslationContext context)
	    throws IOException {
    }

    @Override
    public void generateScript(Writer out, TranslationContext context)
	    throws IOException {
    }

}