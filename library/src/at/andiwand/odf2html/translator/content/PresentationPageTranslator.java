package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.simple.SimpleAttributeTranslator;
import at.andiwand.commons.lwxml.translator.simple.SimpleElementReplacement;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.style.DocumentStyle;


// TODO: improve with complex attribute translator
public class PresentationPageTranslator extends SimpleElementReplacement {
	
	private static final String MASTER_PAGE_ATTRIBUTE = "draw:master-page-name";
	
	private static final String NEW_ELEMENT_NAME = "div";
	private static final String CLASS_ATTRIBUTE = "class";
	
	private final DocumentStyle style;
	
	private String classValue = "";
	
	public PresentationPageTranslator(DocumentStyle style) {
		super(NEW_ELEMENT_NAME);
		
		this.style = style;
		
		addParseAttribute(MASTER_PAGE_ATTRIBUTE);
	}
	
	@Override
	public void addAttributeTranslator(String attributeName,
			SimpleAttributeTranslator translator) {}
	
	@Override
	public void translateEndAttributeList(LWXMLPushbackReader in,
			LWXMLWriter out) throws IOException {
		
	}
	
}