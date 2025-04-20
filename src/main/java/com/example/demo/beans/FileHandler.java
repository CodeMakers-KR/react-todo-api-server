package com.example.demo.beans;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileHandler {

	private String baseDir;
	private boolean enableObfuscation;
	private boolean enableObfuscationHideExt;
	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	
	public void setEnableObfuscation(boolean enableObfuscation) {
		this.enableObfuscation = enableObfuscation;
	}
	
	public void setEnableObfuscationHideExt(boolean enableObfuscationHideExt) {
		this.enableObfuscationHideExt = enableObfuscationHideExt;
	}
	
	public File getStoredFile(String fileName) {
		return new File(baseDir, fileName);
	}
	
	public StoredFile storeFile(MultipartFile multipartFile) {
		String fileName = getObfuscationFileName(multipartFile.getOriginalFilename());
		
		File storePath = new File(baseDir, fileName);
		if (!storePath.getParentFile().exists()) {
			storePath.getParentFile().mkdirs();
		}
		
		try {
			multipartFile.transferTo(storePath);
		} catch (IllegalStateException | IOException e) {
			return null;
		}
		
		return new StoredFile(multipartFile.getOriginalFilename(), storePath);
	}
	
	private String getObfuscationFileName(String fileName) {
		if (enableObfuscation) {
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String obfuscationName = UUID.randomUUID().toString();
			if (enableObfuscationHideExt) {
				return obfuscationName;
			}
			else {
				return obfuscationName + ext;
			}
		}
		
		return fileName;
	}
	
	public class StoredFile {
		private String fileName;
		private String realFileName;
		private String realFilePath;
		private long fileSize;

		StoredFile(String fileName, File storeFile) {
			this.fileName = fileName;
			this.realFileName = storeFile.getName();
			this.realFilePath = storeFile.getAbsolutePath();
			this.fileSize = storeFile.length();
		}

		public String getFileName() {
			return fileName;
		}

		public String getRealFileName() {
			return realFileName;
		}

		public String getRealFilePath() {
			return realFilePath;
		}

		public long getFileSize() {
			return fileSize;
		}

	}
	
}
