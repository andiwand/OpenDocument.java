package at.stefl.opendocument.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.stefl.commons.lwxml.translator.LWXMLElementReplacement;
import at.stefl.opendocument.translator.ScriptGenerator;
import at.stefl.opendocument.translator.StyleGenerator;
import at.stefl.opendocument.translator.context.TranslationContext;

public class DefaultElementTranslator<C extends TranslationContext> extends
	LWXMLElementReplacement<C> implements StyleGenerator<C>,
	ScriptGenerator<C> {

    public DefaultElementTranslator(String elementName) {
	super(elementName);
    }

    @Override
    public void generateStyle(Writer out, C context) throws IOException {
    }

    @Override
    public void generateScript(Writer out, C context) throws IOException {
    }

}