package at.stefl.opendocument.java.translator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import at.stefl.commons.io.CharStreamUtil;

public class StyleScriptUtil {
    
    public static void pipeResource(Class<?> clazz, String name, Writer out)
            throws IOException {
        Reader in = new InputStreamReader(clazz.getResourceAsStream(name));
        CharStreamUtil.writeStreamBuffered(in, out);
    }
    
    public static void pipeClassResource(Class<?> clazz, String extension,
            Writer out) throws IOException {
        String name = clazz.getSimpleName() + "." + extension;
        pipeResource(clazz, name, out);
    }
    
    public static void pipeStyleResource(Class<?> clazz, Writer out)
            throws IOException {
        pipeClassResource(clazz, "css", out);
    }
    
    public static void pipeScriptReource(Class<?> clazz, Writer out)
            throws IOException {
        pipeClassResource(clazz, "js", out);
    }
    
    private StyleScriptUtil() {}
    
}