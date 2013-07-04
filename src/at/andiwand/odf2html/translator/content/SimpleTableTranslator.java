package at.andiwand.odf2html.translator.content;

public class SimpleTableTranslator extends SimpleTableElementTranslator {

    public SimpleTableTranslator() {
	super("table");

	addNewAttribute("border", "0");
	addNewAttribute("cellspacing", "0");
	addNewAttribute("cellpadding", "0");
    }

}