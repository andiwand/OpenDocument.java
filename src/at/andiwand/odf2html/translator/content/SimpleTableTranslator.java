package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.lwxml.SimpleElementReplacement;

public class SimpleTableTranslator extends SimpleElementReplacement {

    private static final String NEW_ELEMENT_NAME = "table";

    public SimpleTableTranslator() {
	super(NEW_ELEMENT_NAME);

	addNewAttribute("border", "0");
	addNewAttribute("cellspacing", "0");
	addNewAttribute("cellpadding", "0");
    }

}