package com.IdeaBox.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.IdeaBox.models.sugestoes.FileEstudoViabilidade;
import com.IdeaBox.repository.FileRepository;

@Service
public class FileStorageService {

	@Autowired
	private FileRepository fr;

	public FileEstudoViabilidade store(MultipartFile file) throws IOException {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileEstudoViabilidade File = new FileEstudoViabilidade(fileName, file.getContentType(), file.getBytes());

		return fr.save(File);
	}
	
	public FileEstudoViabilidade getFile(String Id) {
		return fr.findById(Id).get();
	}
	
	public Stream<FileEstudoViabilidade> getAllFiles() {
		return fr.findAll().stream();
	}
}
