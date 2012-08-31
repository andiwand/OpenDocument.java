package at.andiwand.odf2html.translator.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.common.lwxml.LWXMLAttribute;
import at.andiwand.odf2html.css.StyleProperty;
import at.andiwand.odf2html.css.StyleSheet;
import at.andiwand.odf2html.css.StyleSheetParser;
import at.andiwand.odf2html.css.StyleSheetWriter;


public class DocumentStyle {
	
	private static final String DEFAULT_STYLE_SHEET_NAME = "default.css";
	private static final StyleSheet DEFAULT_STYLE_SHEET;
	
	static {
		try {
			DEFAULT_STYLE_SHEET = loadStyleSheet(DEFAULT_STYLE_SHEET_NAME,
					DocumentStyle.class);
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
	
	protected static StyleSheet loadStyleSheet(String name, Class<?> location)
			throws IOException {
		InputStream in = location.getResourceAsStream(name);
		StyleSheetParser styleSheetParser = new StyleSheetParser();
		return styleSheetParser.parse(in);
	}
	
	public static String translateStyleName(String name) {
		return name.replaceAll("\\.", "_");
	}
	
	private Map<String, Set<String>> styleInheritance = new HashMap<String, Set<String>>();
	private final StyleSheetWriter styleOut;
	
	public DocumentStyle(StyleSheetWriter styleOut) throws IOException {
		if (styleOut == null) throw new NullPointerException();
		this.styleOut = styleOut;
		
		styleOut.writeSheet(DEFAULT_STYLE_SHEET);
	}
	
	public List<String> getStyleParents(String name) {
		Set<String> parents = styleInheritance.get(name);
		if (parents == null) return null;
		return new ArrayList<String>(parents);
	}
	
	public String getStyleReference(String name) {
		Set<String> parents = styleInheritance.get(name);
		if (parents == null) return null;
		
		String result;
		
		result = translateStyleName(name);
		for (String parent : parents) {
			result = translateStyleName(parent) + " " + result;
		}
		
		return result;
	}
	
	public LWXMLAttribute getStyleAttribute(String name) {
		String reference = getStyleReference(name);
		if (reference == null) reference = "null";
		return new LWXMLAttribute("class", reference);
	}
	
	private void addStyleInheritance(String name, String... parents) {
		Set<String> parentSet = new LinkedHashSet<String>();
		
		for (String parent : parents) {
			Set<String> parentsParentSet = styleInheritance.get(parent);
			if (parentsParentSet == null) continue;
			
			parentSet.add(parent);
			parentSet.addAll(parentsParentSet);
		}
		
		styleInheritance.put(name, parentSet);
	}
	
	public void writeClass(String name, String... parents) throws IOException {
		if (styleOut.isDefinitionStarted()) styleOut.writeEndDefinition();
		styleOut.writeStartDefinition("." + translateStyleName(name));
		addStyleInheritance(name, parents);
	}
	
	public void writeProperty(StyleProperty property) throws IOException {
		styleOut.writeProperty(property);
	}
	
	public void writeProperty(String name, String value) throws IOException {
		styleOut.writeProperty(name, value);
	}
	
}