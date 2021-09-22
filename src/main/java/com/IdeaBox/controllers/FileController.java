package com.IdeaBox.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.IdeaBox.models.sugestoes.FileEstudoViabilidade;
import com.IdeaBox.models.sugestoes.ResponseFile;
import com.IdeaBox.models.sugestoes.ResponseMessage;
import com.IdeaBox.service.FileStorageService;

@Controller
@CrossOrigin("http://localhost:8099")
public class FileController {

	@Autowired
	private FileStorageService storageService;

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") long id) {
		String message = "";

		try {
			storageService.store(file, id);
			message = "Arquivo enviado com sucesso: " + file.getOriginalFilename();
			return "redirect:/pendentes";
		} catch (Exception e) {
			message = "Não foi possivel enviar o arquivo: " + file.getOriginalFilename() + "!";
			e.printStackTrace();
			return "redirect:/pendentes";
		}
	}

	@GetMapping("/arquivos")
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/arquivos/")
					.path(dbFile.getId()).toUriString();

			return new ResponseFile(dbFile.getNome(), fileDownloadUri, dbFile.getTipo(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/arquivo/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		FileEstudoViabilidade file = storageService.getFile(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getNome() + "\"")
				.body(file.getData());
	}

	@GetMapping("/arquivo")
	public String verConteudo(@RequestParam("path") String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String linha = "";

		while (true) {
			if (linha != null) {
				linha = reader.readLine();
				return linha;
			} else {
				break;
			}

		}
		
		return linha;
	}
	
	//Colocar o getMapping aqui(Primeiro testar o metodo acima)
	public String metodo2(@RequestParam("arquivo")FileEstudoViabilidade file) throws IOException {
		

        byte[] bytes = Files.readAllBytes(file.toPath());

        String textoDoArquivo = new String(bytes, "UTF-8");

        return textoDoArquivo;
	}
}
