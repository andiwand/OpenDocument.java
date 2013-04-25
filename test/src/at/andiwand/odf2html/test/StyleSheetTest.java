package at.andiwand.odf2html.test;

import at.andiwand.odf2html.css.StyleDefinition;
import at.andiwand.odf2html.css.StyleSheet;

public class StyleSheetTest {

    public static void main(String[] args) {
	StyleSheet styleSheet = new StyleSheet();

	StyleDefinition all = new StyleDefinition();
	all.addProperty("text-weight", "bold");
	all.addProperty("font-family", "Arial");

	StyleDefinition table = new StyleDefinition();
	table.addProperty("margin", "0px");

	styleSheet.addDefinition("*", all);
	styleSheet.addDefinition("table", table);

	System.out.println(styleSheet);
    }

}