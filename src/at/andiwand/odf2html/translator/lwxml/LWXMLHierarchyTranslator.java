package at.andiwand.odf2html.translator.lwxml;

import java.io.IOException;
import java.util.Map;

import at.andiwand.commons.io.StreamableStringMap;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.collection.OrderedPair;

// TODO: fix generics
public class LWXMLHierarchyTranslator<C> extends
	LWXMLTranslator<LWXMLReader, LWXMLWriter, C> {

    private StreamableStringMap<LWXMLElementTranslator> elementTranslators = new StreamableStringMap<LWXMLElementTranslator>();

    private StreamableStringMap<LWXMLAttributeTranslator> staticAttributeTranslators = new StreamableStringMap<LWXMLAttributeTranslator>();

    public void addElementTranslator(String element, String newElement) {
	addElementTranslator(element,
		new LWXMLElementReplacement<C>(newElement));
    }

    public void addElementTranslator(String element,
	    LWXMLElementTranslator<? super C> translator) {
	if (element == null)
	    throw new NullPointerException();
	elementTranslators.put(element, translator);

	for (Map.Entry<String, LWXMLAttributeTranslator> entry : staticAttributeTranslators
		.entrySet()) {
	    translator.addAttributeTranslator(entry.getKey(), entry.getValue());
	}
    }

    public void addStaticAttributeTranslator(String attribute,
	    String newAttribute) {
	addStaticAttributeTranslator(attribute,
		new LWXMLStaticAttributeTranslator<C>(newAttribute));
    }

    public void addStaticAttributeTranslator(String attribute,
	    LWXMLAttributeTranslator<? super C> attributeTranslator) {
	if (attributeTranslator == null)
	    throw new NullPointerException();
	staticAttributeTranslators.put(attribute, attributeTranslator);

	for (LWXMLElementTranslator<C> translator : elementTranslators.values()) {
	    translator.addAttributeTranslator(attribute, attributeTranslator);
	}
    }

    public void removeElementTranslator(String element) {
	elementTranslators.remove(element);
    }

    public void removeStaticAttributeTranslator(String attribute) {
	staticAttributeTranslators.remove(attribute);
    }

    @Override
    public void translate(LWXMLReader in, LWXMLWriter out, C context)
	    throws IOException {
	LWXMLPushbackReader pin = new LWXMLPushbackReader(in);

	OrderedPair<String, LWXMLElementTranslator> match = null;

	LWXMLEvent event;
	while (true) {
	    event = pin.readEvent();
	    if (event == LWXMLEvent.END_DOCUMENT)
		break;

	    switch (event) {
	    case START_ELEMENT:
	    case END_ELEMENT:
		match = elementTranslators.match(pin);
	    case END_EMPTY_ELEMENT:
		if (match == null)
		    break;

		String elementName = match.getElement1();
		LWXMLElementTranslator<? super C> translator = match
			.getElement2();

		pin.unreadEvent(elementName);
		translator.translate(pin, out, context);

		break;
	    case CHARACTERS:
		out.writeEvent(LWXMLEvent.CHARACTERS);
		out.write(pin);
		break;
	    default:
		break;
	    }
	}
    }

}