package at.andiwand.odf2html.translator.style;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLBranchDelegationReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.odf.OpenDocument;


public abstract class DocumentStyleTranslator<T extends DocumentStyle> {
	
	private static final String GENERAL_STYLE_NAME = "style:style";
	
	private static final String DOCUMENT_STYLE_ELEMENT_NAME = "office:styles";
	
	private Map<String, StyleElementTranslator<? super T>> elementTranslatorMap = new HashMap<String, StyleElementTranslator<? super T>>();
	
	public DocumentStyleTranslator() {
		addElementTranslator(GENERAL_STYLE_NAME,
				new GeneralStyleElementTranslator());
	}
	
	public void addElementTranslator(String name,
			StyleElementTranslator<? super T> elementTranslator) {
		if (name == null) throw new NullPointerException();
		if (elementTranslator == null) throw new NullPointerException();
		
		elementTranslatorMap.put(name, elementTranslator);
	}
	
	public void removeElementTranslator(String name) {
		elementTranslatorMap.remove(name);
	}
	
	public abstract T newDocumentStyle(StyleSheetWriter styleOut)
			throws IOException;
	
	public void translate(OpenDocument document, T out) throws IOException {
		LWXMLReader in = new LWXMLStreamReader(document.getStyles());
		
		LWXMLUtil.flushUntilStartElement(in, DOCUMENT_STYLE_ELEMENT_NAME);
		translate(in, out);
	}
	
	public void translate(LWXMLReader in, T out) throws IOException {
		LWXMLBranchDelegationReader din = new LWXMLBranchDelegationReader(in);
		
		while (true) {
			LWXMLEvent event = din.readEvent();
			
			switch (event) {
			case START_ELEMENT:
				String startElement = din.readValue();
				StyleElementTranslator<? super T> elementTranslator = elementTranslatorMap
						.get(startElement);
				LWXMLReader bin = din.getBranchReader();
				if (elementTranslator == null) break;
				
				elementTranslator.translate(bin, out);
				
				break;
			case END_EMPTY_ELEMENT:
			case END_ELEMENT:
			case END_DOCUMENT:
				return;
			}
		}
	}
	
}