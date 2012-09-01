package at.andiwand.odf2html.translator.content;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import at.andiwand.common.lwxml.LWXMLIllegalEventException;
import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.common.lwxml.translator.LWXMLTranslatorException;
import at.andiwand.common.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.common.lwxml.writer.LWXMLWriter;


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
		String path = LWXMLUtil.parseSingleAttributes(in, PATH_ATTRIBUTE_NAME);
		if (path == null) throw new LWXMLTranslatorException();
		// TODO: remove and implement path resolver
		path = path.replaceAll("\\./", "");
		
		out.writeAttribute("style", "width: 100%; heigth: 100%");
		
		// TODO: improve
		if (path.contains(OBJECT_REPLACEMENT_STRING)) {
			out.writeAttribute("alt", "charts are not implemented so far... :/");
			return;
		}
		
		System.out.println(path);
		System.out.println(new File(path).getPath());
		
		out.writeAttribute("alt", path);
		
		out.writeAttribute("src", "");
		writeSource(path, out);
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
	
	public abstract void writeSource(String path, Writer out)
			throws IOException;
	
}