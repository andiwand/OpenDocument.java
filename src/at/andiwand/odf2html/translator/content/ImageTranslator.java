package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.io.Writer;

import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.LWXMLTranslatorException;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;


// TODO: implement charts
public abstract class ImageTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "img";
	private static final String PATH_ATTRIBUTE_NAME = "xlink:href";
	
	private static final String OBJECT_REPLACEMENT_STRING = "ObjectReplacement";
	
	public ImageTranslator() {
		super(NEW_ELEMENT_NAME);
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		String name = LWXMLUtil.parseSingleAttributes(in, PATH_ATTRIBUTE_NAME);
		if (name == null) throw new LWXMLTranslatorException();
		// TODO: remove and implement path resolver
		name = name.replaceAll("\\./", "");
		
		out.writeAttribute("style", "width: 100%; heigth: 100%");
		
		// TODO: improve
		if (name.contains(OBJECT_REPLACEMENT_STRING)) {
			out.writeAttribute("alt", "charts are not implemented so far... :/");
		} else {
			out.writeAttribute("alt", "Image not found: " + name);
			
			out.writeAttribute("src", "");
			writeSource(name, out);
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