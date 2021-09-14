package com.IdeaBox.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.IdeaBox.models.cargos.Cargo;
import com.IdeaBox.models.sugestoes.Sugestao;
import com.IdeaBox.models.usuarios.Colaborador;
import com.IdeaBox.repository.CargoRepository;
import com.IdeaBox.repository.ColaboradorRepository;
import com.IdeaBox.repository.SugestaoRepository;
import com.IdeaBox.service.ServiceUsuario;

@Controller
public class ColaboradorController {
	@Autowired
	private ColaboradorRepository cr;

	@Autowired
	private SugestaoRepository sr;

	@Autowired
	private CargoRepository crg;
	
	@Autowired
	private ServiceUsuario su;

	@GetMapping("/colaboradores")
	public ModelAndView listaSugestao() {
		ModelAndView mv = new ModelAndView("colaborador/listaColaboradores");
		Iterable<Colaborador> colaboradores = cr.findAll();
		Iterable<Cargo> cargos = crg.findAll();
		mv.addObject("cargos", cargos);
		Cargo cargo = new Cargo();
		mv.addObject("cargoColaborador", cargo);
		
		mv.addObject("colaboradores", colaboradores);
		return mv;
	}

	@RequestMapping("/deletar")
	public String deletarColaborador(long Id) {
		Colaborador colaborador = cr.findById(Id);
		cr.delete(colaborador);
		return "redirect:/colaboradores";
	}

	@PostMapping("/editarNome")
	public String editarColaborador(@RequestParam long id, @RequestParam("nome") String nome) {
		Colaborador colaborador = cr.findById(id);
		colaborador.setNome(nome);
		cr.save(colaborador);
		return "redirect:/colaboradores";
	}

	@PostMapping("/editarCpf")
	public String editarCpf(@RequestParam long id, @RequestParam("cpf") String cpf) {
		Colaborador colaborador = cr.findById(id);
		colaborador.setCpf(cpf);
		cr.save(colaborador);
		return "redirect:/colaboradores";
	}
	
	@PostMapping("/editarCargo")
	public String editarCargo(@RequestParam long id, @RequestParam long cargoId) {
		Cargo cargo1 = crg.findById(cargoId);
		Colaborador colaborador = cr.findById(id);
		colaborador.setCargo(cargo1);
		cr.save(colaborador);
		crg.save(cargo1);
		return "redirect:/colaboradores";
	}
	
	@PostMapping("/editarLogin")
	public String editarLogin(@RequestParam long id, @RequestParam("login") String login) {
		Colaborador colaborador = cr.findById(id);
		colaborador.setLogin(login);
		cr.save(colaborador);
		return "redirect:/colaboradores";
	}
	
	@PostMapping("/editarEmail")
	public String editarEmail(@RequestParam long id, @RequestParam("email") String email) {
		Colaborador colaborador = cr.findById(id);
		colaborador.setEmail(email);
		cr.save(colaborador);
		return "redirect:/colaboradores";
	}
}
