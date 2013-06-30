package at.andiwand.odf2html.translator.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.commons.lwxml.LWXMLAttribute;
import at.andiwand.odf2html.css.StyleProperty;
import at.andiwand.odf2html.css.StyleSheet;
import at.andiwand.odf2html.css.StyleSheetParser;
import at.andiwand.odf2html.css.StyleSheetWriter;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;

// TODO: make use of multiple class selector
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

	try {
	    StyleSheetParser styleSheetParser = new StyleSheetParser();
	    return styleSheetParser.parse(in);
	} finally {
	    in.close();
	}
    }

    private static String translateStyleName(String name,
	    StylePropertyGroup group) {
	return name.replaceAll("\\.", "_") + "-" + group;
    }

    private Map<String, Set<String>> styleInheritance = new HashMap<String, Set<String>>();
    private final StyleSheetWriter styleOut;

    public DocumentStyle(StyleSheetWriter styleOut) throws IOException {
	if (styleOut == null)
	    throw new NullPointerException();
	this.styleOut = styleOut;

	styleOut.writeSheet(DEFAULT_STYLE_SHEET);
    }

    public List<String> getStyleParents(String name) {
	Set<String> parents = styleInheritance.get(name);
	if (parents == null)
	    return null;
	return new ArrayList<String>(parents);
    }

    // TODO: remove?
    public String getStyleReference(String name) {
	Set<String> parents = styleInheritance.get(name);
	if (parents == null)
	    return null;

	String result = "";

	for (StylePropertyGroup group : StylePropertyGroup.values()) {
	    result += translateStyleName(name, group) + " ";
	}

	for (String parent : parents) {
	    for (StylePropertyGroup group : StylePropertyGroup.values()) {
		result += " " + translateStyleName(parent, group);
	    }
	}

	return result;
    }

    public String getStyleReference(String name, StylePropertyGroup group) {
	Set<String> parents = styleInheritance.get(name);
	if (parents == null)
	    return null;

	String result = translateStyleName(name, group);
	for (String parent : parents) {
	    result += " " + translateStyleName(parent, group);
	}

	return result;
    }

    // TODO: remove?
    public LWXMLAttribute getStyleAttribute(String name) {
	String reference = getStyleReference(name);
	if (reference == null)
	    reference = "null";
	return new LWXMLAttribute("class", reference);
    }

    public LWXMLAttribute getStyleAttribute(String name,
	    StylePropertyGroup group) {
	String reference = getStyleReference(name, group);
	if (reference == null)
	    reference = "null";
	return new LWXMLAttribute("class", reference);
    }

    public void addStyleInheritance(String name, Collection<String> parents) {
	Set<String> parentSet = new LinkedHashSet<String>();

	for (String parent : parents) {
	    parentSet.add(parent);

	    Set<String> parentsParentSet = styleInheritance.get(parent);
	    if (parentsParentSet != null)
		parentSet.addAll(parentsParentSet);
	}

	styleInheritance.put(name, parentSet);
    }

    public void writeClass(String name, StylePropertyGroup group)
	    throws IOException {
	if (styleOut.isDefinitionStarted())
	    styleOut.writeEndDefinition();
	styleOut.writeStartDefinition("." + translateStyleName(name, group));
    }

    public void writeProperty(StyleProperty property) throws IOException {
	styleOut.writeProperty(property);
    }

    public void writeProperty(String name, String value) throws IOException {
	styleOut.writeProperty(name, value);
    }

    public void close() throws IOException {
	if (styleOut.isDefinitionStarted())
	    styleOut.writeEndDefinition();
    }

}