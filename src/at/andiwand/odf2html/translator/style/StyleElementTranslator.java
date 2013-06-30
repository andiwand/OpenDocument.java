package at.andiwand.odf2html.translator.style;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLReader;

public abstract class StyleElementTranslator<T extends DocumentStyle> {

    public abstract void translate(LWXMLReader in, T out) throws IOException;

}