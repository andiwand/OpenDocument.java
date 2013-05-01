package at.andiwand.odf2html.translator.style;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.io.StreamableStringMap;
import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.util.array.ArrayUtil;
import at.andiwand.commons.util.collection.CollectionUtil;
import at.andiwand.commons.util.collection.OrderedPair;
import at.andiwand.odf2html.css.StyleProperty;
import at.andiwand.odf2html.translator.style.property.StaticGeneralPropertyTranslator;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

public class DefaultStyleElementTranslator extends
	StyleElementTranslator<DocumentStyle> {

    private static final String[] DIRECTION_SUFFIXES = { "", "-top", "-right",
	    "-bottom", "-left" };

    private static final String NAME_ATTRIBUTE_NAME = "style:name";
    private static final String FAMILY_ATTRIBUTE_NAME = "style:family";
    private static final String PARENT_ATTRIBUTE_NAME = "style:parent-style-name";

    private static final Set<String> DEFAULT_PARENT_ATTRIBUTES = ArrayUtil
	    .toCollection(new LinkedHashSet<String>(2), FAMILY_ATTRIBUTE_NAME,
		    PARENT_ATTRIBUTE_NAME);

    private final Set<String> parentAttributes;

    public DefaultStyleElementTranslator() {
	this(DEFAULT_PARENT_ATTRIBUTES);
    }

    public DefaultStyleElementTranslator(Set<String> parentAttributes) {
	this.parentAttributes = new LinkedHashSet<String>(parentAttributes);
    }

    private static String getToAttributeNameByColon(String attribute) {
	int colonIndex = attribute.indexOf(':');
	return (colonIndex == -1) ? attribute : attribute
		.substring(colonIndex + 1);
    }

    private final EnumMap<StylePropertyGroup, StreamableStringMap<PropertyTranslator>> groupAttributeTranslatorMap = new EnumMap<StylePropertyGroup, StreamableStringMap<PropertyTranslator>>(
	    StylePropertyGroup.class);

    private StreamableStringMap<PropertyTranslator> getAttributeTranslators(
	    StylePropertyGroup group) {
	StreamableStringMap<PropertyTranslator> result = groupAttributeTranslatorMap
		.get(group);

	if (result == null) {
	    result = new StreamableStringMap<PropertyTranslator>();
	    groupAttributeTranslatorMap.put(group, result);
	}

	return result;
    }

    public void addPropertyTranslator(String attribute, StylePropertyGroup group) {
	addPropertyTranslator(attribute, group,
		getToAttributeNameByColon(attribute));
    }

    public void addPropertyTranslator(String attribute,
	    StylePropertyGroup group, String property) {
	addPropertyTranslator(attribute, group,
		new StaticGeneralPropertyTranslator(property));
    }

    public void addPropertyTranslator(String attribute,
	    StylePropertyGroup group, PropertyTranslator translator) {
	if (attribute == null)
	    throw new NullPointerException();
	if (translator == null)
	    throw new NullPointerException();

	getAttributeTranslators(group).put(attribute, translator);
    }

    public void addDirectionPropertyTranslator(String attribute,
	    StylePropertyGroup group, PropertyTranslator translator) {
	for (String directionPrefix : DIRECTION_SUFFIXES) {
	    addPropertyTranslator(attribute + directionPrefix, group,
		    translator);
	}
    }

    public void addDirectionPropertyTranslator(String attribute,
	    StylePropertyGroup group) {
	addDirectionPropertyTranslator(attribute, group,
		getToAttributeNameByColon(attribute));
    }

    public void addDirectionPropertyTranslator(String attribute,
	    StylePropertyGroup group, String property) {
	for (String directionPrefix : DIRECTION_SUFFIXES) {
	    addPropertyTranslator(attribute + directionPrefix, group, property
		    + directionPrefix);
	}
    }

    public void addParentAttribute(String attribute) {
	parentAttributes.add(attribute);
    }

    public void removePropertyTranslator(String attribute) {
	groupAttributeTranslatorMap.remove(attribute);
    }

    public void removeDirectionPropertyTranslator(String attribute) {
	for (String directionPrefix : DIRECTION_SUFFIXES) {
	    removePropertyTranslator(attribute + directionPrefix);
	}
    }

    public void removeParentAttribute(String attribute) {
	parentAttributes.remove(attribute);
    }

    protected String getStyleName(Map<String, String> attributes) {
	String name = attributes.get(NAME_ATTRIBUTE_NAME);
	String family = attributes.get(FAMILY_ATTRIBUTE_NAME);

	if (name == null)
	    return family;
	return name;
    }

    protected LinkedHashSet<String> getParentStyles(
	    Map<String, String> attributes) {
	LinkedHashSet<String> result = new LinkedHashSet<String>();
	CollectionUtil.getNotNull(attributes, parentAttributes, result);
	return result;
    }

    @Override
    public void translate(LWXMLReader in, DocumentStyle out) throws IOException {
	// TODO: optimize: parse only needed attributes
	Map<String, String> attributes = LWXMLUtil.parseAllAttributes(in);

	String name = getStyleName(attributes);
	if (name == null)
	    return;
	Collection<String> parents = getParentStyles(attributes);
	out.addStyleInheritance(name, parents);

	StylePropertyGroup group = null;

	loop: while (true) {
	    LWXMLEvent event = in.readEvent();

	    switch (event) {
	    case START_ELEMENT:
		// TODO: optimize
		String element = in.readValue();
		group = StylePropertyGroup.getGroupByElement(element);
		if (group != null)
		    out.writeClass(name, group);
		break;
	    case END_ELEMENT:
		group = null;
		break;
	    case ATTRIBUTE_NAME:
		if (group == null)
		    break;
		OrderedPair<String, PropertyTranslator> match = groupAttributeTranslatorMap
			.get(group).match(in);

		if (match != null) {
		    String attributeName = match.getElement1();
		    PropertyTranslator translator = match.getElement2();

		    String attributeValue = in.readFollowingValue();
		    StyleProperty property = translator.translate(
			    attributeName, attributeValue);
		    if (property == null)
			break;
		    out.writeProperty(property);
		}

		break;
	    case END_DOCUMENT:
		break loop;
	    default:
		break;
	    }
	}
    }

}