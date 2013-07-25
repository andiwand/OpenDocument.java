package at.stefl.opendocument.java.translator.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.stefl.commons.lwxml.LWXMLAttribute;
import at.stefl.opendocument.java.css.StyleProperty;
import at.stefl.opendocument.java.css.StyleSheet;
import at.stefl.opendocument.java.css.StyleSheetParser;
import at.stefl.opendocument.java.css.StyleSheetWriter;
import at.stefl.opendocument.java.translator.style.property.StylePropertyGroup;

public class DocumentStyle {

    private static final String GROUP_PREFIX = "_group-";

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

    private static String escapeStyleName(String name) {
	return name.replaceAll("\\.", "_");
    }

    private static String escapeStyleGroup(StylePropertyGroup group) {
	return GROUP_PREFIX + group;
    }

    private Map<String, Set<String>> styleInheritance = new HashMap<String, Set<String>>();
    private final StyleSheetWriter styleOut;

    public DocumentStyle(StyleSheetWriter styleOut) throws IOException {
	if (styleOut == null)
	    throw new NullPointerException();
	this.styleOut = styleOut;
    }

    public List<String> getStyleParents(String name) {
	Set<String> parents = styleInheritance.get(name);
	if (parents == null)
	    return null;
	return new ArrayList<String>(parents);
    }

    public String getStyleReference(String name, StylePropertyGroup... groups) {
	Set<String> parents = styleInheritance.get(name);
	if (parents == null)
	    return null;
	// TODO: redesign
	if ((groups == null) || (groups.length == 0)
		|| ((groups.length == 1) && (groups[0] == null)))
	    groups = StylePropertyGroup.values();

	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < groups.length; i++) {
	    builder.append(escapeStyleGroup(groups[i]));
	    builder.append(' ');
	}

	builder.append(escapeStyleName(name));
	for (String parent : parents) {
	    builder.append(' ');
	    builder.append(escapeStyleName(parent));
	}

	return builder.toString();
    }

    public LWXMLAttribute getStyleAttribute(String name,
	    StylePropertyGroup... groups) {
	String reference = getStyleReference(name, groups);
	if (reference == null)
	    reference = "null";
	return new LWXMLAttribute("class", reference);
    }

    public void addStyleInheritance(String name, Set<String> parents) {
	Set<String> parentSet = new HashSet<String>();

	for (String parent : parents) {
	    parentSet.add(parent);

	    Set<String> parentsParentSet = styleInheritance.get(parent);
	    // TODO: log null
	    if (parentsParentSet != null)
		parentSet.addAll(parentsParentSet);
	}

	styleInheritance.put(name, parentSet);
    }

    public void writeClass(String name, StylePropertyGroup group)
	    throws IOException {
	if (styleOut.isDefinitionStarted())
	    styleOut.writeEndDefinition();
	styleOut.writeStartDefinition("." + escapeStyleName(name) + "."
		+ escapeStyleGroup(group));
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