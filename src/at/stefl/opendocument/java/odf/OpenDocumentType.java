package at.stefl.opendocument.java.odf;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import at.stefl.commons.util.array.ArrayUtil;
import at.stefl.commons.util.collection.CollectionUtil;
import at.stefl.commons.util.object.ObjectTransformer;

// TODO: provide "application/vnd.oasis.opendocument" check
public enum OpenDocumentType {

    TEXT(new String[] { "odt", "fodt" },
	    "application/vnd.oasis.opendocument.text", OpenDocumentText.class) {
	@Override
	OpenDocumentText getDocument(OpenDocumentFile documentFile) {
	    return new OpenDocumentText(documentFile);
	}
    },
    SPREADSHEET(new String[] { "ods", "fods" },
	    "application/vnd.oasis.opendocument.spreadsheet",
	    OpenDocumentSpreadsheet.class) {
	@Override
	OpenDocumentSpreadsheet getDocument(OpenDocumentFile documentFile) {
	    return new OpenDocumentSpreadsheet(documentFile);
	}
    },
    PRESENTATION(new String[] { "odp", "fodp" },
	    "application/vnd.oasis.opendocument.presentation",
	    OpenDocumentPresentation.class) {
	@Override
	OpenDocumentPresentation getDocument(OpenDocumentFile documentFile) {
	    return new OpenDocumentPresentation(documentFile);
	}
    };

    private static final ObjectTransformer<OpenDocumentType, Class<? extends OpenDocument>> CLASS_KEY_GENERATOR = new ObjectTransformer<OpenDocumentType, Class<? extends OpenDocument>>() {
	@Override
	public Class<? extends OpenDocument> transform(OpenDocumentType value) {
	    return value.documentClass;
	}
    };

    private static final Map<Class<? extends OpenDocument>, OpenDocumentType> BY_CLASS_MAP = CollectionUtil
	    .toHashMap(CLASS_KEY_GENERATOR, values());

    public static OpenDocumentType getByClass(
	    Class<? extends OpenDocument> clazz) {
	return BY_CLASS_MAP.get(clazz);
    }

    public static OpenDocumentType getByMimeType(String mimeType) {
	if (!mimeType.startsWith("application/vnd.oasis.opendocument"))
	    throw new IllegalMimeTypeException(mimeType);

	for (OpenDocumentType type : values()) {
	    if (type.validMimeType(mimeType))
		return type;
	}

	throw new UnsupportedMimeTypeException(mimeType);
    }

    public static OpenDocument getSuitableDocument(OpenDocumentFile documentFile)
	    throws IOException {
	return getByMimeType(documentFile.getMimetype()).getDocument(
		documentFile);
    }

    private final Set<String> extensions;
    private final String mimeType;
    private final Class<? extends OpenDocument> documentClass;

    private OpenDocumentType(String[] extensions, String mimetype,
	    Class<? extends OpenDocument> documentClass) {
	this.extensions = Collections.unmodifiableSet(ArrayUtil.toCollection(
		new LinkedHashSet<String>(extensions.length), extensions));
	this.mimeType = mimetype;
	this.documentClass = documentClass;
    }

    public Set<String> getExtension() {
	return extensions;
    }

    public String getMimeType() {
	return mimeType;
    }

    public Class<? extends OpenDocument> getDocumentClass() {
	return documentClass;
    }

    public boolean validMimeType(String mimeType) {
	return mimeType.startsWith(this.mimeType);
    }

    abstract OpenDocument getDocument(OpenDocumentFile documentFile);

}