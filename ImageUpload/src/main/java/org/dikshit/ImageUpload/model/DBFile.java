package org.dikshit.ImageUpload.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class DBFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private Long id;

	@Column(name = "filename")
	private String fileName;

	@Column(name = "filetype")
	private String fileType;

	@Lob
	@Column(name = "data")
	private byte[] data;

	public DBFile() {

	}

	public DBFile(String fileName, String fileType, byte[] data) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	
}
