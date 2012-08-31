package at.andiwand.odf2html.translator.content;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import at.andiwand.common.lwxml.LWXMLUtil;
import at.andiwand.common.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.common.lwxml.translator.LWXMLTranslatorException;
import at.andiwand.common.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.common.lwxml.writer.LWXMLWriter;
import at.andiwand.common.util.ArrayUtil;


public class FrameTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "div";
	private static final String WIDTH_ATTRIBUTE_NAME = "svg:width";
	private static final String HEIGHT_ATTRIBUTE_NAME = "svg:height";
	private static final Set<String> ATTRIBUTE_NAMES = ArrayUtil.toHashSet(
			WIDTH_ATTRIBUTE_NAME, HEIGHT_ATTRIBUTE_NAME);
	
	public FrameTranslator() {
		super(NEW_ELEMENT_NAME);
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		Map<String, String> attributes = LWXMLUtil.parseAttributes(in,
				ATTRIBUTE_NAMES);
		if (attributes.size() != ATTRIBUTE_NAMES.size())
			throw new LWXMLTranslatorException();
		
		out.writeAttribute("style", "width:"
				+ attributes.get(WIDTH_ATTRIBUTE_NAME) + ";height:"
				+ attributes.get(HEIGHT_ATTRIBUTE_NAME) + ";");
	}
	
}