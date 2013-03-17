package at.andiwand.odf2html.translator.style;

public abstract class DefaultStyleTranslator<T extends DocumentStyle> extends
		DocumentStyleTranslator<T> {
	
	private static final String GENERAL_STYLE_ELEMENT_NAME = "style:style";
	
	public DefaultStyleTranslator() {
		addElementTranslator(GENERAL_STYLE_ELEMENT_NAME,
				new GeneralStyleElementTranslator());
	}
	
}