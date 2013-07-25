package at.stefl.opendocument.java.translator.context;

import at.stefl.commons.io.CountingInputStream;
import at.stefl.opendocument.java.odf.OpenDocument;
import at.stefl.opendocument.java.odf.OpenDocumentFile;
import at.stefl.opendocument.java.translator.settings.TranslationSettings;
import at.stefl.opendocument.java.translator.style.DocumentStyle;

public abstract class TranslationContext {

    private CountingInputStream counter;
    private long size;

    public double getProgress() {
	if (counter == null)
	    return 0;
	return (double) counter.count() / size;
    }

    public abstract OpenDocument getDocument();

    public OpenDocumentFile getDocumentFile() {
	return getDocument().getDocumentFile();
    }

    public abstract DocumentStyle getStyle();

    public abstract TranslationSettings getSettings();

    public void setCounter(CountingInputStream counter) {
	this.counter = counter;
    }

    public void setDocument(OpenDocument document) {
	size = document.getContentSize();
    }

    public abstract void setStyle(DocumentStyle style);

    public abstract void setSettings(TranslationSettings settings);

}