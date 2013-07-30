package at.stefl.opendocument.java.translator.settings;

import at.stefl.commons.math.vector.Vector2i;
import at.stefl.opendocument.java.util.FileCache;

public class TranslationSettings {

    private FileCache cache;
    private ImageStoreMode imageStoreMode;
    private Vector2i maximalTableDimension;

    public TranslationSettings() {
    }

    public TranslationSettings(TranslationSettings settings) {
	this.cache = settings.cache;
	this.imageStoreMode = settings.imageStoreMode;
	this.maximalTableDimension = settings.maximalTableDimension;
    }

    public FileCache getCache() {
	return cache;
    }

    public ImageStoreMode getImageStoreMode() {
	return imageStoreMode;
    }

    public Vector2i getMaximalTableDimension() {
	return maximalTableDimension;
    }

    public void setCache(FileCache cache) {
	this.cache = cache;
    }

    public void setImageStoreMode(ImageStoreMode imageStoreMode) {
	this.imageStoreMode = imageStoreMode;
    }

    public void setMaximalTableDimension(Vector2i maximalTableDimension) {
	this.maximalTableDimension = maximalTableDimension;
    }

}