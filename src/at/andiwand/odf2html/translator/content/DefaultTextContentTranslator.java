package at.andiwand.odf2html.translator.content;

import at.andiwand.odf2html.translator.context.TranslationContext;

public class DefaultTextContentTranslator<C extends TranslationContext> extends
	DefaultContentTranslator<C> {

    public DefaultTextContentTranslator() {
	// TODO: translate list style
	addElementTranslator("text:list", new DefaultElementTranslator("ul"));
	addElementTranslator("text:list-item", new DefaultElementTranslator(
		"li"));

	addElementTranslator("table:table", new SimpleTableTranslator());
	addElementTranslator("table:table-column",
		new SimpleTableElementTranslator("col"));
	addElementTranslator("table:table-row",
		new SimpleTableElementTranslator("tr"));
	addElementTranslator("table:table-cell",
		new SimpleTableElementTranslator("td"));
    }

}