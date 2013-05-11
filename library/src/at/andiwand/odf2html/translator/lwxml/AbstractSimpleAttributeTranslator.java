package at.andiwand.odf2html.translator.lwxml;

public abstract class AbstractSimpleAttributeTranslator implements
	SimpleAttributeTranslator {

    @Override
    public boolean accept(SimpleElementTranslator translator) {
	return true;
    }

}