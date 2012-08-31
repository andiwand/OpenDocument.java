package at.andiwand.odf2html.css;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import at.andiwand.common.io.CharacterStreamUtil;
import at.andiwand.common.io.FluidInputStreamReader;
import at.andiwand.common.io.UntilCharacterReader;
import at.andiwand.common.util.StringUtil;


public class StyleSheetParser {
	
	public StyleSheet parse(File file) throws IOException {
		return parse(new FileReader(file));
	}
	
	public StyleSheet parse(InputStream in) throws IOException {
		return parse(new FluidInputStreamReader(in));
	}
	
	public StyleSheet parse(Reader in) throws IOException {
		StyleSheet result = new StyleSheet();
		
		while (true) {
			String selector = parseSelector(in);
			if (selector == null) return result;
			
			result.addDefinition(selector, parseDefinition(in));
		}
	}
	
	private String parseSelector(Reader in) throws IOException {
		int c = CharacterStreamUtil.flushWhitespace(in);
		if (c == -1) return null;
		
		return ((char) c)
				+ StringUtil.trimRight(CharacterStreamUtil.readUntilChar(in,
						'{'));
	}
	
	private StyleDefinition parseDefinition(Reader in) throws IOException {
		StyleDefinition result = new StyleDefinition();
		
		in = new UntilCharacterReader(in, '}');
		
		while (true) {
			StyleProperty property = parseProperty(in);
			if (property == null) break;
			result.addProperty(property);
		}
		
		return result;
	}
	
	private StyleProperty parseProperty(Reader in) throws IOException {
		int c = CharacterStreamUtil.flushWhitespace(in);
		if (c == -1) return null;
		
		String name = ((char) c)
				+ StringUtil.trimRight(CharacterStreamUtil.readUntilChar(in,
						':'));
		
		c = CharacterStreamUtil.flushWhitespace(in);
		
		String value = ((char) c)
				+ StringUtil.trimRight(CharacterStreamUtil.readUntilChar(in,
						';'));
		
		return new StyleProperty(name, value);
	}
	
}