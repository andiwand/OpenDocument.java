package at.andiwand.odf2html.translator.style;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.lwxml.LWXMLEvent;
import at.andiwand.commons.lwxml.LWXMLUtil;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.util.ArrayUtil;
import at.andiwand.odf2html.css.StyleProperty;


public class GeneralStyleElementTranslator extends
		StyleElementTranslator<DocumentStyle> {
	
	private static final String[] DIRECTION_SUFFIXES = {"", "-top", "-right",
			"-bottom", "-left"};
	
	private static final String NAME_ATTRIBUTE_NAME = "style:name";
	private static final String FAMILY_ATTRIBUTE_NAME = "style:family";
	private static final String PARENT_ATTRIBUTE_NAME = "style:parent-style-name";
	private static final Set<String> ATTRIBUTES = ArrayUtil.toHashSet(
			NAME_ATTRIBUTE_NAME, FAMILY_ATTRIBUTE_NAME, PARENT_ATTRIBUTE_NAME);
	
	private static String getToAttributeNameByColon(String fromAttributeName) {
		int colonIndex = fromAttributeName.indexOf(':');
		return (colonIndex == -1) ? fromAttributeName : fromAttributeName
				.substring(colonIndex + 1);
	}
	
	private final Map<String, GeneralPropertyTranslator> attributeTranslatorMap = new HashMap<String, GeneralPropertyTranslator>();
	
	public GeneralStyleElementTranslator() {
		addDirectionPropertyTranslator("fo:margin");
		addDirectionPropertyTranslator("fo:padding");
		addPropertyTranslator("style:width");
		addPropertyTranslator("style:height");
		addPropertyTranslator("fo:font-size");
		addPropertyTranslator("style:font-name", "font-family");
		addPropertyTranslator("fo:font-weight");
		addPropertyTranslator("fo:font-style");
		addPropertyTranslator("fo:font-size");
		addPropertyTranslator("fo:text-shadow");
		addPropertyTranslator("fo:text-align");
		addPropertyTranslator("style:vertical-align");
		addPropertyTranslator("fo:color");
		addPropertyTranslator("fo:background-color");
		addPropertyTranslator("style:vertical-align");
		addPropertyTranslator("style:column-width", "width");
		addPropertyTranslator("style:row-height", "height");
		addDirectionPropertyTranslator("fo:border");
		
		addPropertyTranslator("style:text-underline-style",
				new UnderlinePropertyTranslator());
		addPropertyTranslator("style:text-line-through-style",
				new LineThroughPropertyTranslator());
		addPropertyTranslator("style:text-position",
				new VerticalAlignPropertyTranslator());
		addDirectionPropertyTranslator("fo:border",
				new BorderPropertyTranslator());
	}
	
	public void addPropertyTranslator(String attributeName) {
		addPropertyTranslator(attributeName,
				getToAttributeNameByColon(attributeName));
	}
	
	public void addPropertyTranslator(String attributeName, String propertyName) {
		addPropertyTranslator(attributeName,
				new StaticGeneralPropertyTranslator(propertyName));
	}
	
	public void addPropertyTranslator(String attributeName,
			GeneralPropertyTranslator translator) {
		if (attributeName == null) throw new NullPointerException();
		if (translator == null) throw new NullPointerException();
		
		attributeTranslatorMap.put(attributeName, translator);
	}
	
	public void addDirectionPropertyTranslator(String attributeName,
			GeneralPropertyTranslator translator) {
		for (String directionPrefix : DIRECTION_SUFFIXES) {
			addPropertyTranslator(attributeName + directionPrefix, translator);
		}
	}
	
	public void addDirectionPropertyTranslator(String attributeName) {
		addDirectionPropertyTranslator(attributeName,
				getToAttributeNameByColon(attributeName));
	}
	
	public void addDirectionPropertyTranslator(String attributeName,
			String propertyName) {
		for (String directionPrefix : DIRECTION_SUFFIXES) {
			addPropertyTranslator(attributeName + directionPrefix, propertyName
					+ directionPrefix);
		}
	}
	
	public void removePropertyTranslator(String attributeName) {
		attributeTranslatorMap.remove(attributeName);
	}
	
	public void removeDirectionPropertyTranslator(String attributeName) {
		for (String directionPrefix : DIRECTION_SUFFIXES) {
			removePropertyTranslator(attributeName + directionPrefix);
		}
	}
	
	@Override
	public void translate(LWXMLReader in, DocumentStyle out) throws IOException {
		Map<String, String> attributes = LWXMLUtil.parseAttributes(in,
				ATTRIBUTES);
		String name = attributes.get(NAME_ATTRIBUTE_NAME);
		String family = attributes.get(FAMILY_ATTRIBUTE_NAME);
		String parent = attributes.get(PARENT_ATTRIBUTE_NAME);
		
		if (name == null) {
			if (family == null) return;
			
			name = family;
			family = null;
		}
		
		out.writeClass(name, family, parent);
		
		loop:
		while (true) {
			LWXMLEvent event = in.readEvent();
			
			switch (event) {
			case ATTRIBUTE_NAME:
				String attributeName = in.readValue();
				GeneralPropertyTranslator translator = attributeTranslatorMap
						.get(attributeName);
				if (translator == null) break;
				
				String attributeValue = in.readFollowingValue();
				StyleProperty property = translator.translate(attributeName,
						attributeValue);
				out.writeProperty(property);
				
				break;
			case END_DOCUMENT:
				break loop;
			}
		}
	}
	
}