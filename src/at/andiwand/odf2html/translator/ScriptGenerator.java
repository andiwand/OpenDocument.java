package at.andiwand.odf2html.translator;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.odf2html.translator.context.TranslationContext;

public interface ScriptGenerator<C extends TranslationContext> {

    // TODO: use script writer
    public void generateScript(Writer out, C context) throws IOException;

}