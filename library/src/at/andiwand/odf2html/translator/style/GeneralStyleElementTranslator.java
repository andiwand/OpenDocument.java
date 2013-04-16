package at.andiwand.odf2html.translator.style;

public class GeneralStyleElementTranslator extends
	DefaultStyleElementTranslator {

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
	addPropertyTranslator("fo:page-width", "width");
	addPropertyTranslator("fo:page-height", "height");

	addPropertyTranslator("style:text-underline-style",
		new UnderlinePropertyTranslator());
	addPropertyTranslator("style:text-line-through-style",
		new LineThroughPropertyTranslator());
	addPropertyTranslator("style:text-position",
		new VerticalAlignPropertyTranslator());
	addDirectionPropertyTranslator("fo:border",
		new BorderPropertyTranslator());

	addPropertyTranslator("fo:margin", new MarginPropertyTranslator());
    }

}