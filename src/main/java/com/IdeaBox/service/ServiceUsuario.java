package com.IdeaBox.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IdeaBox.exceptions.CriptoExistException;
import com.IdeaBox.exceptions.EmailExistException;

import com.IdeaBox.exceptions.ServiceExce;
import com.IdeaBox.models.usuarios.Administrador;
import com.IdeaBox.models.usuarios.Colaborador;
import com.IdeaBox.repository.ColaboradorRepository;
import com.IdeaBox.util.Util;

@Service
public class ServiceUsuario {
	@Autowired
	private ColaboradorRepository cr;
	
	public void salvarColaborador(Colaborador colaborador) throws Exception {
		try {
			if(cr.findByEmail(colaborador.getEmail()) != null) {
				throw new EmailExistException("Esse email já está cadastro para: " + colaborador.getEmail());
			}
			
			colaborador.setSenha(Util.md5(colaborador.getSenha()));
			
		} catch (NoSuchAlgorithmException e) {
			throw new CriptoExistException("Erro na criptografia na senha");
		}
		
		cr.save(colaborador);
	}
	
	public Colaborador loginColaborador(String login, String senha) throws ServiceExce{
		
		Colaborador colaboradorLogin = cr.findLogin(login, senha);
		return colaboradorLogin;
		
	}
	
	public Administrador loginAdm(String login, String senha) throws ServiceExce{
		Administrador admLogin = cr.findByLoginA(login, senha);
		return admLogin;
	}
}