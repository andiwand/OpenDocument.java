package at.andiwand.odf2html.test;

import java.io.IOException;
import java.io.InputStream;

import at.andiwand.commons.io.CharArrayWriter;
import at.andiwand.commons.io.FluidInputStreamReader;
import at.andiwand.commons.lwxml.reader.LWXMLReader;
import at.andiwand.commons.lwxml.reader.LWXMLStreamReader;
import at.andiwand.commons.lwxml.writer.LWXMLStreamWriter;
import at.andiwand.commons.lwxml.writer.LWXMLWriter;
import at.andiwand.odf2html.translator.lwxml.LWXMLHierarchyTranslator;

public class SimpleLWXMLTranslatorTest {

    public static void main(String[] args) throws IOException {
	InputStream inputStream = SimpleLWXMLTranslatorTest.class
		.getResourceAsStream("test.xml");
	LWXMLReader in = new LWXMLStreamReader(new FluidInputStreamReader(
		inputStream));

	CharArrayWriter writer = new CharArrayWriter();
	LWXMLWriter out = new LWXMLStreamWriter(writer);

	LWXMLHierarchyTranslator<Object> lwxmlTranslator = new LWXMLHierarchyTranslator<Object>();
	lwxmlTranslator.addElementTranslator("html", "xml");
	lwxmlTranslator.addElementTranslator("head", "kopf");
	lwxmlTranslator.addElementTranslator("title", "name");
	lwxmlTranslator.addElementTranslator("body", "bauch");
	lwxmlTranslator.addElementTranslator("empty", "leer");
	lwxmlTranslator.addStaticAttributeTranslator("name", "attribute");
	lwxmlTranslator.translate(in, out, null);

	out.close();

	System.out.println(writer);
    }

}