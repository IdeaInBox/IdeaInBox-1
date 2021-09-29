package com.IdeaBox.controllers;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.IdeaBox.dto.ClassificacaoRequest;
import com.IdeaBox.models.sugestoes.FileEstudoViabilidade;
import com.IdeaBox.models.sugestoes.Status_Sugestao;
import com.IdeaBox.models.sugestoes.Sugestao;
import com.IdeaBox.models.usuarios.Colaborador;
import com.IdeaBox.models.usuarios.Gerente;
import com.IdeaBox.repository.ColaboradorRepository;
import com.IdeaBox.repository.FileRepository;
import com.IdeaBox.repository.GerenteRepository;
import com.IdeaBox.repository.SugestaoRepository;

@Controller
public class SugestaoController {
	@Autowired
	private SugestaoRepository sr;

	@Autowired
	private ColaboradorRepository cr;
	
	@Autowired
	private GerenteRepository gr;

	@Autowired
	private FileRepository fr;

	@RequestMapping(value = "/enviar", method = RequestMethod.POST)
	public String form(Sugestao sugestao, HttpSession session) {
		if (session.getAttribute("colaboradorLogado") != null) {
			Colaborador colaborador = (Colaborador) session.getAttribute("colaboradorLogado");
			sugestao.setColaborador(colaborador);
			sugestao.setTesteEnviada(true);
			colaborador.getSugestoes().add(sugestao);
			cr.save(colaborador);
			colaborador.getSugestoes().clear();
		} else {
			Gerente gerente = (Gerente) session.getAttribute("gerenteLogado");
			sugestao.setColaborador(gerente);
			sugestao.setTesteEnviada(true);
			gerente.getSugestoes().add(sugestao);
			gr.save(gerente);
			gerente.getSugestoes().clear();
		}

		return "redirect:/timeline/1";

	}

	@RequestMapping("/deletarSugestao")
	public String deletarSugestao(long Id) {
		Sugestao sugestao = sr.findById(Id);
		sr.delete(sugestao);
		return "redirect:/profile";
	}

	@RequestMapping("/aprovarSugestao")
	public String aprovarSugestao(long Id) {
		Sugestao sugestao = sr.findById(Id);
		if (sugestao.getStatus() == Status_Sugestao.EM_ANALISE_RH) {
			sugestao.setStatus(Status_Sugestao.APROVADO_PELO_RH);
		} else {
			sugestao.setStatus(Status_Sugestao.APROVADO_GERENCIA);
		}

		sr.save(sugestao);
		return "redirect:/pendentes";
	}

	@RequestMapping("/reprovarSugestao")
	public String reprovarSugestao(long Id) {
		Sugestao sugestao = sr.findById(Id);
		if (sugestao.getStatus() == Status_Sugestao.EM_ANALISE_RH) {
			sugestao.setStatus(Status_Sugestao.REPROVADO_PELO_RH);
		}

		sr.save(sugestao);
		return "redirect:/pendentes";
	}

	@PostMapping("/avaliar")
	public String avaliarSugestao(@RequestParam(required = true) long id, ClassificacaoRequest classificacao,
			HttpSession session) {
		Sugestao sugestao = sr.findById(id);
		
		if (session.getAttribute("colaboradorLogado") != null) {
			Colaborador colaborador = (Colaborador) session.getAttribute("colaboradorLogado");
			if (!cr.findByAvaliacao(sugestao.getId(), colaborador.getId()).isEmpty() || sugestao.isTesteEnviada() == false) {
				return "redirect:/timeline/1";
			}
			colaborador.setTotalSugestoesAvaliadas(colaborador.getTotalSugestoesAvaliadas() + 1);
			sugestao.setTotalDeAvaliacoes(sugestao.getTotalDeAvaliacoes() + 1);
			sugestao.setClassificacao(
					(sugestao.getClassificacao() + classificacao.getClassificacao()) / sugestao.getTotalDeAvaliacoes());
			sugestao.getAvaliadores().add(colaborador);
			colaborador.getSugestoesAvaliadas().add(sugestao);
			sr.save(sugestao);
			cr.save(colaborador);
			colaborador.getSugestoesAvaliadas().clear();

		}
		
		if(session.getAttribute("gerenteLogado") != null){
			Gerente colaborador = (Gerente) session.getAttribute("gerenteLogado");
			if (!gr.findByAvaliacao(sugestao.getId(), colaborador.getId()).isEmpty() || sugestao.isTesteEnviada() == false) {
				return "redirect:/timeline/1";
			}
			colaborador.setTotalSugestoesAvaliadas(colaborador.getTotalSugestoesAvaliadas() + 1);
			sugestao.setTotalDeAvaliacoes(sugestao.getTotalDeAvaliacoes() + 1);
			sugestao.setClassificacao(
					(sugestao.getClassificacao() + classificacao.getClassificacao()) / sugestao.getTotalDeAvaliacoes());
			sugestao.getAvaliadores().add(colaborador);
			colaborador.getSugestoesAvaliadas().add(sugestao);
			sr.save(sugestao);
			gr.save(colaborador);
			colaborador.getSugestoesAvaliadas().clear();
		}
		return "redirect:/timeline/1";
	}

	@PostMapping("/editar")
	public String editarSugestao(@RequestParam long id, @RequestParam("texto") String texto) {
		Sugestao sugestao = sr.findById(id);
		sugestao.setTexto(texto);
		sr.save(sugestao);
		return "redirect:/profile";
	}

	@GetMapping("/pendentes")
	public ModelAndView sugestaoPendente() {
		Iterable<Sugestao> sugestoes = sr.findAllInAnalise();
		ModelAndView mv = new ModelAndView("sugestoesPendentes");
		mv.addObject("sugestoes", sugestoes);
		return mv;
	}

	@GetMapping("/topsugestoes")
	public ModelAndView sugestoesMaisVotadas() {
		Iterable<Sugestao> sugestoes = sr.findTop();
		ModelAndView mv = new ModelAndView("sugestoesMaisVotadas");
		mv.addObject("sugestoes", sugestoes);
		return mv;
	}

	@RequestMapping("/moveToAdm")
	public String moverParaOAdm(long id) {
		Sugestao sugestao = sr.findById(id);
		sugestao.setStatus(Status_Sugestao.TOP_TREND);
		sr.save(sugestao);
		return "redirect:/topsugestoes";
	}

	@GetMapping("/sugestaoADM")
	public ModelAndView sugestoesAnaliseAdm() {
		Iterable<Sugestao> sugestoes = sr.findAllInTopTrend();
		ModelAndView mv = new ModelAndView("sugestaoAdmAnalisar");
		mv.addObject("sugestoes", sugestoes);
		return mv;
	}

	@GetMapping("/arquivos")
	public ModelAndView arquivos() {
		ModelAndView mv = new ModelAndView("arquivos");
		Iterable<FileEstudoViabilidade> list = fr.findAll();

		mv.addObject("files", list);

		return mv;
	}
	
	@RequestMapping("/arquivar")
	public String arquivar(long id) {
		Sugestao sugestao = sr.findById(id);
		sugestao.setStatus(Status_Sugestao.ARQUIVADO);
		sr.save(sugestao);
		return "redirect:/topsugestoes";
	}
}
