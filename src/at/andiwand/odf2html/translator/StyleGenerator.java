package at.andiwand.odf2html.translator;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.odf2html.translator.context.TranslationContext;

public interface StyleGenerator<C extends TranslationContext> {

    // TODO: use style writer
    public void generateStyle(Writer out, C context) throws IOException;

}