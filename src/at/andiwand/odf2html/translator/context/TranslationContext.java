package at.andiwand.odf2html.translator.context;

import at.andiwand.commons.io.CountingInputStream;
import at.andiwand.odf2html.odf.OpenDocument;
import at.andiwand.odf2html.odf.OpenDocumentFile;
import at.andiwand.odf2html.translator.settings.TranslationSettings;
import at.andiwand.odf2html.translator.style.DocumentStyle;

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