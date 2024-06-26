package org.dikshit.ImageUpload.service;

import java.io.IOException;

import org.dikshit.ImageUpload.exception.FileStorageException;
import org.dikshit.ImageUpload.exception.MyFileNotFoundException;
import org.dikshit.ImageUpload.model.DBFile;
import org.dikshit.ImageUpload.repository.DBFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DBFileStorageService  {

	@Autowired
	private DBFileRepository dbFileRepository;

	
	public DBFile storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());

			return dbFileRepository.save(dbFile);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	
	public DBFile getFile(String fileId) {
		return dbFileRepository.findById(Long.parseLong(fileId))
				.orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
	}

}
