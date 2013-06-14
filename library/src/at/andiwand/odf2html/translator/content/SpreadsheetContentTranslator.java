package at.andiwand.odf2html.translator.content;

import java.io.IOException;

import at.andiwand.commons.math.vector.Vector2i;
import at.andiwand.odf2html.odf.OpenDocumentSpreadsheet;
import at.andiwand.odf2html.translator.style.SpreadsheetStyle;
import at.andiwand.odf2html.util.FileCache;

public class SpreadsheetContentTranslator extends DefaultContentTranslator {

    public SpreadsheetContentTranslator(OpenDocumentSpreadsheet document,
	    SpreadsheetStyle style) throws IOException {
	this(document, style, (Vector2i) null);
    }

    public SpreadsheetContentTranslator(OpenDocumentSpreadsheet document,
	    SpreadsheetStyle style, Vector2i maxTableDimension)
	    throws IOException {
	this(document, style, new InlineImageTranslator(
		document.getDocumentFile()), maxTableDimension);
    }

    public SpreadsheetContentTranslator(OpenDocumentSpreadsheet document,
	    SpreadsheetStyle style, FileCache fileCache) throws IOException {
	this(document, style, fileCache, null);
    }

    public SpreadsheetContentTranslator(OpenDocumentSpreadsheet document,
	    SpreadsheetStyle style, FileCache fileCache,
	    Vector2i maxTableDimension) throws IOException {
	this(document, style, new CachedImageTranslator(
		document.getDocumentFile(), fileCache), maxTableDimension);
    }

    public SpreadsheetContentTranslator(OpenDocumentSpreadsheet document,
	    SpreadsheetStyle style, ImageTranslator imageTranslator,
	    Vector2i maxTableDimension) throws IOException {
	super(style, imageTranslator);

	addElementTranslator("draw:frame", new FrameTranslator());

	addElementTranslator("table:tracked-changes", new NothingTranslator());
	SpreadsheetTableTranslator tableTranslator = new SpreadsheetTableTranslator(
		style, this, document.getTableDimensionMap(), maxTableDimension);
	addElementTranslator("table:table", tableTranslator);

	SpreadsheetParagraphTranslator paragraphTranslator = new SpreadsheetParagraphTranslator(
		this);
	addElementTranslator("text:p", paragraphTranslator);
	addElementTranslator("text:h", paragraphTranslator);
    }

    @Override
    protected void translateStyleAttribute(
	    StyleAttributeTranslator styleAttributeTranslator) {
	super.translateStyleAttribute(styleAttributeTranslator);

	addStaticAttributeTranslator("draw:text-style-name",
		styleAttributeTranslator);
    }

}