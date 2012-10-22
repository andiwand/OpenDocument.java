package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;


public class FrameTranslator extends SimpleElementReplacement {
	
	private static final String NEW_ELEMENT_NAME = "div";
	private static final String X_ATTRIBUTE_NAME = "svg:x";
	private static final String Y_ATTRIBUTE_NAME = "svg:y";
	private static final String WIDTH_ATTRIBUTE_NAME = "svg:width";
	private static final String HEIGHT_ATTRIBUTE_NAME = "svg:height";
	
	// TODO: remove?
	private final boolean translatePosition;
	
	public FrameTranslator() {
		this(true);
	}
	
	public FrameTranslator(boolean translatePosition) {
		super(NEW_ELEMENT_NAME);
		
		this.translatePosition = translatePosition;
		
		addParseAttribute(X_ATTRIBUTE_NAME);
		addParseAttribute(Y_ATTRIBUTE_NAME);
		addParseAttribute(WIDTH_ATTRIBUTE_NAME);
		addParseAttribute(HEIGHT_ATTRIBUTE_NAME);
	}
	
	@Override
	public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
			throws IOException {
		super.translateAttributeList(in, out);
		
		String style = "";
		
		if (translatePosition) {
			String x = getCurrentParsedAttribute(X_ATTRIBUTE_NAME);
			String y = getCurrentParsedAttribute(Y_ATTRIBUTE_NAME);
			
			style += "position:absolute;";
			if (x != null) style += "left:" + x + ";";
			if (y != null) style += "top:" + y + ";";
		}
		
		String width = getCurrentParsedAttribute(WIDTH_ATTRIBUTE_NAME);
		String height = getCurrentParsedAttribute(HEIGHT_ATTRIBUTE_NAME);
		
		// TODO: stupid stuff
		// if ((width == null) || (height == null))
		// 	throw new IllegalStateException();
		
		style += "width:" + width + ";";
		style += "height:" + height + ";";
		
		out.writeAttribute("style", style);
	}
	
}