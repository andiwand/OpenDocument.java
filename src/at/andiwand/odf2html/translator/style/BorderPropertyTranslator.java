package at.andiwand.odf2html.translator.style;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.odf2html.css.StyleProperty;


public class BorderPropertyTranslator implements GeneralPropertyTranslator {
	
	private static final double DEFAULT_LIMIT = 0.8;
	
	private static final Pattern PT_PATTERN = Pattern
			.compile("(\\d+(\\.\\d+)?)pt");
	
	private final double limit;
	
	public BorderPropertyTranslator() {
		this(DEFAULT_LIMIT);
	}
	
	public BorderPropertyTranslator(double limit) {
		this.limit = limit;
	}
	
	@Override
	public StyleProperty translate(String name, String value) {
		int colonIndex = name.indexOf(':');
		if (colonIndex != -1) name = name.substring(colonIndex + 1);
		
		Matcher matcher = PT_PATTERN.matcher(value);
		
		if (matcher.find()) {
			double pt = Double.parseDouble(matcher.group(1));
			
			if (pt < limit) {
				value = value.substring(0, matcher.start()) + "1px"
						+ value.substring(matcher.end());
			}
		}
		
		return new StyleProperty(name, value);
	}
	
}