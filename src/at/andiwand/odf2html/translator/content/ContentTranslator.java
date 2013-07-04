package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.commons.lwxml.translator.LWXMLElementTranslator;
import at.andiwand.commons.lwxml.translator.LWXMLHierarchyTranslator;
import at.andiwand.odf2html.translator.ScriptGenerator;
import at.andiwand.odf2html.translator.StyleGenerator;
import at.andiwand.odf2html.translator.context.TranslationContext;

public abstract class ContentTranslator<C extends TranslationContext> extends
	LWXMLHierarchyTranslator<C> implements StyleGenerator<C>,
	ScriptGenerator<C> {

    @Override
    public void generateStyle(Writer out, C context) throws IOException {
	for (LWXMLElementTranslator<? super C> lwxmlTranslator : elementTranslators()) {
	    if (!(lwxmlTranslator instanceof DefaultElementTranslator))
		continue;
	    DefaultElementTranslator translator = (DefaultElementTranslator) lwxmlTranslator;
	    translator.generateStyle(out, context);
	}
    }

    @Override
    public void generateScript(Writer out, C context) throws IOException {
	for (LWXMLElementTranslator<? super C> lwxmlTranslator : elementTranslators()) {
	    if (!(lwxmlTranslator instanceof DefaultElementTranslator))
		continue;
	    DefaultElementTranslator translator = (DefaultElementTranslator) lwxmlTranslator;
	    translator.generateScript(out, context);
	}
    }

}