package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.odf.OpenDocumentFile;

// TODO: implement charts
// TODO: skip empty images
public abstract class ImageTranslator extends SimpleElementReplacement {

    private static final String NEW_ELEMENT_NAME = "img";
    private static final String PATH_ATTRIBUTE_NAME = "xlink:href";

    private static final String OBJECT_REPLACEMENT_STRING = "ObjectReplacement";

    protected final OpenDocumentFile documentFile;

    public ImageTranslator(OpenDocumentFile documentFile) {
	super(NEW_ELEMENT_NAME);

	this.documentFile = documentFile;
    }

    @Override
    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	String name = LWXMLUtil.parseSingleAttribute(in, PATH_ATTRIBUTE_NAME);
	// TODO: log
	if (name == null)
	    return;

	out.writeAttribute("style", "width: 100%; heigth: 100%");

	// TODO: improve
	if (name.contains(OBJECT_REPLACEMENT_STRING)) {
	    out.writeAttribute("alt", "Charts are not implemented so far... :/");
	} else {
	    out.writeAttribute("alt", "Image not found: " + name);

	    out.writeAttribute("src", "");
	    if (documentFile.isFile(name))
		writeSource(name, out);
	    else
		out.write(name);
	}

    }

    @Override
    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	LWXMLUtil.flushBranch(in);

	out.writeEndEmptyElement();
    }

    @Override
    public void translateEndElement(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	throw new LWXMLIllegalEventException(in);
    }

    public abstract void writeSource(String name, Writer out)
	    throws IOException;

}