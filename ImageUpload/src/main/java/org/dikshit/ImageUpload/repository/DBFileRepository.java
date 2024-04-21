package org.dikshit.ImageUpload.repository;

import org.dikshit.ImageUpload.model.DBFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface  DBFileRepository extends JpaRepository<DBFile, Long>{

	
}
