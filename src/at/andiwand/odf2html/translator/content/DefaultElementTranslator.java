package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.commons.lwxml.translator.LWXMLElementReplacement;
import at.andiwand.odf2html.translator.ScriptGenerator;
import at.andiwand.odf2html.translator.StyleGenerator;
import at.andiwand.odf2html.translator.context.TranslationContext;

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