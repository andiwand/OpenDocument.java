package at.andiwand.odf2html.translator.style;

import at.andiwand.odf2html.translator.style.property.BorderPropertyTranslator;
import at.andiwand.odf2html.translator.style.property.LineThroughPropertyTranslator;
import at.andiwand.odf2html.translator.style.property.MarginPropertyTranslator;
import at.andiwand.odf2html.translator.style.property.StylePropertyGroup;
import at.andiwand.odf2html.translator.style.property.UnderlinePropertyTranslator;
import at.andiwand.odf2html.translator.style.property.VerticalAlignPropertyTranslator;

public class GeneralStyleElementTranslator extends
	DefaultStyleElementTranslator {

    public GeneralStyleElementTranslator() {
	addPropertyTranslator("fo:text-align", StylePropertyGroup.PARAGRAPH);
	addDirectionPropertyTranslator("fo:margin",
		StylePropertyGroup.PARAGRAPH);
	addDirectionPropertyTranslator("fo:padding",
		StylePropertyGroup.PARAGRAPH);
	addDirectionPropertyTranslator("fo:border",
		StylePropertyGroup.PARAGRAPH);
	addPropertyTranslator("style:column-width",
		StylePropertyGroup.PARAGRAPH, "width");
	addPropertyTranslator("style:row-height", StylePropertyGroup.PARAGRAPH,
		"height");
	addDirectionPropertyTranslator("fo:border",
		StylePropertyGroup.PARAGRAPH, new BorderPropertyTranslator());
	addPropertyTranslator("fo:margin", StylePropertyGroup.PARAGRAPH,
		new MarginPropertyTranslator());

	addPropertyTranslator("fo:font-size", StylePropertyGroup.TEXT);
	addPropertyTranslator("style:font-name", StylePropertyGroup.TEXT,
		"font-family");
	addPropertyTranslator("fo:font-weight", StylePropertyGroup.TEXT);
	addPropertyTranslator("fo:font-style", StylePropertyGroup.TEXT);
	addPropertyTranslator("fo:font-size", StylePropertyGroup.TEXT);
	addPropertyTranslator("fo:text-shadow", StylePropertyGroup.TEXT);
	addPropertyTranslator("fo:color", StylePropertyGroup.TEXT);
	addPropertyTranslator("fo:background-color", StylePropertyGroup.TEXT);
	addPropertyTranslator("style:vertical-align", StylePropertyGroup.TEXT);
	addPropertyTranslator("style:text-underline-style",
		StylePropertyGroup.TEXT, new UnderlinePropertyTranslator());
	addPropertyTranslator("style:text-line-through-style",
		StylePropertyGroup.TEXT, new LineThroughPropertyTranslator());
	addPropertyTranslator("style:text-position", StylePropertyGroup.TEXT,
		new VerticalAlignPropertyTranslator());

	addPropertyTranslator("style:width", StylePropertyGroup.GRAPHIC);
	addPropertyTranslator("style:height", StylePropertyGroup.GRAPHIC);
	addPropertyTranslator("draw:fill-color", StylePropertyGroup.GRAPHIC,
		"background-color");

	addPropertyTranslator("fo:page-width", StylePropertyGroup.PAGE_LAYOUT,
		"width");
	addPropertyTranslator("fo:page-height", StylePropertyGroup.PAGE_LAYOUT,
		"height");
    }

}