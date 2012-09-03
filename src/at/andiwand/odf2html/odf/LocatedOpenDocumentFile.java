package at.andiwand.odf2html.odf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;


public class LocatedOpenDocumentFile extends OpenDocumentFile {
	
	private final ZipFile zipFile;
	
	public LocatedOpenDocumentFile(File file) throws IOException {
		try {
			zipFile = new ZipFile(file);
		} catch (ZipException e) {
			// TODO: log e
			throw new IOException("zip exception");
		}
	}
	
	public File getFile() {
		return zipFile.getFile();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getFileList() throws IOException {
		try {
			List<FileHeader> fileHeaders = zipFile.getFileHeaders();
			List<String> result = new ArrayList<String>(fileHeaders.size());
			
			for (FileHeader fileHeader : fileHeaders) {
				result.add(fileHeader.getFileName());
			}
			
			return result;
		} catch (ZipException e) {
			// TODO: log e
			throw new IOException("zip exception");
		}
	}
	
	// TODO: exception
	@Override
	@SuppressWarnings("unchecked")
	protected InputStream getRawFileStream(String path) throws IOException {
		try {
			List<FileHeader> fileHeaders = zipFile.getFileHeaders();
			
			for (FileHeader fileHeader : fileHeaders) {
				if (!path.equals(fileHeader.getFileName())) continue;
				return zipFile.getInputStream(fileHeader);
			}
			
			throw new FileNotFoundException("file does not exist");
		} catch (ZipException e) {
			// TODO: log e
			throw new IOException("zip exception");
		}
	}
	
}