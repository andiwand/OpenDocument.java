package at.andiwand.odf2html.translator.lwxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.io.StreamableStringMap;
import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.LWXMLIllegalEventException;
import at.andiwand.commons.lwxml.reader.LWXMLPushbackReader;
import at.andiwand.commons.lwxml.translator.LWXMLPushbackTranslator;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.commons.util.collection.OrderedPair;

// TODO: implement remove methods
public abstract class SimpleElementTranslator extends LWXMLPushbackTranslator {

    private final StreamableStringMap<SimpleAttributeTranslator> attributeTranslatorMap = new StreamableStringMap<SimpleAttributeTranslator>();

    private final Set<String> parseAttributes = new HashSet<String>();
    private final Map<String, String> currentParsedAttributes = new HashMap<String, String>();

    private final List<LWXMLAttribute> newAttributes = new ArrayList<LWXMLAttribute>();

    public Map<String, String> getCurrentParsedAttributes() {
	return currentParsedAttributes;
    }

    public String getCurrentParsedAttribute(String attributeName) {
	return currentParsedAttributes.get(attributeName);
    }

    public boolean addAttributeTranslator(String attributeName,
	    String newAttributeName) {
	return addAttributeTranslator(attributeName,
		new SimpleStaticAttributeTranslator(newAttributeName));
    }

    public boolean addAttributeTranslator(String attributeName,
	    SimpleAttributeTranslator translator) {
	if (attributeName == null)
	    throw new NullPointerException();
	if (translator == null)
	    throw new NullPointerException();

	attributeTranslatorMap.put(attributeName, translator);
	return true;
    }

    public void addParseAttribute(String attributeName) {
	if (attributeName == null)
	    throw new NullPointerException();

	parseAttributes.add(attributeName);
	if (!attributeTranslatorMap.containsKey(attributeName))
	    attributeTranslatorMap.put(attributeName, null);
    }

    public void addNewAttribute(LWXMLAttribute attribute) {
	if (attribute == null)
	    throw new NullPointerException();

	newAttributes.add(attribute);
    }

    public void addNewAttribute(String name, String value) {
	addNewAttribute(new LWXMLAttribute(name, value));
    }

    @Override
    public final void translate(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	LWXMLEvent event = in.readEvent();

	switch (event) {
	case START_ELEMENT:
	    translateStartElement(in, out);
	    translateAttributeList(in, out);
	    translateEndAttributeList(in, out);
	    translateChildren(in, out);
	    break;
	case END_EMPTY_ELEMENT:
	case END_ELEMENT:
	    translateEndElement(in, out);
	    break;
	default:
	    throw new LWXMLIllegalEventException(event);
	}
    }

    public abstract void translateStartElement(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException;

    public LWXMLAttribute translateAttribute(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException {
	OrderedPair<String, SimpleAttributeTranslator> match = attributeTranslatorMap
		.match(in);
	if (match == null)
	    return null;

	String attributeName = match.getElement1();
	String attributeValue = in.readFollowingValue();

	if (parseAttributes.contains(attributeName))
	    currentParsedAttributes.put(attributeName, attributeValue);

	SimpleAttributeTranslator attributeTranslator = match.getElement2();
	if (attributeTranslator == null)
	    return null;
	return attributeTranslator.translate(attributeName, attributeValue);
    }

    public void translateAttributeList(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
	for (LWXMLAttribute attribute : newAttributes) {
	    out.writeAttribute(attribute);
	}

	loop: while (true) {
	    LWXMLEvent event = in.readEvent();

	    switch (event) {
	    case ATTRIBUTE_NAME:
		LWXMLAttribute attribute = translateAttribute(in, out);
		if (attribute != null)
		    out.writeAttribute(attribute);
	    case ATTRIBUTE_VALUE:
		break;
	    case END_ATTRIBUTE_LIST:
		break loop;
	    default:
		throw new LWXMLIllegalEventException(event);
	    }
	}
    }

    public void translateEndAttributeList(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException {
	currentParsedAttributes.clear();

	out.writeEvent(LWXMLEvent.END_ATTRIBUTE_LIST);
    }

    public void translateChildren(LWXMLPushbackReader in, LWXMLWriter out)
	    throws IOException {
    }

    public abstract void translateEndElement(LWXMLPushbackReader in,
	    LWXMLWriter out) throws IOException;

}