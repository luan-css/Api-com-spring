package com.algaworks.socialbooks.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.socialbooks.domain.Comentario;
import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.repository.LivrosRepository;
import com.algaworks.socialbooks.services.LivrosService;
import com.algaworks.socialbooks.services.exceptions.LivroNaoEncontradoException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(tags = "Livros")
@RestController
@RequestMapping("/livros")
public class LivrosResources {
	@Autowired
	private LivrosService livrosService;
	@ApiOperation(value = "Lista livros")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Livro>> listar() {
		return ResponseEntity.status(HttpStatus.OK).body(livrosService.listar());
	}
	@ApiOperation(value = "Cadastra livros")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> salvar(@Valid @RequestBody Livro livro) {
		livro = livrosService.salvar(livro);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(livro.getId()).toUri();
	
		return ResponseEntity.created(uri).build();
	}
	@ApiOperation(value = "Busca livros por id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> buscar(@PathVariable("id") Long id) {
		Livro livro = livrosService.buscar(id);
		return ResponseEntity.status(HttpStatus.OK).body(livro);
	}
	@ApiOperation(value = "Exclui um livro")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		livrosService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	@ApiOperation(value = "Atualizar um livro")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizar(@RequestBody Livro livro, @PathVariable("id") Long id) {
		livro.setId(id);
		livrosService.atualizar(livro);
		return ResponseEntity.noContent().build();
	}
	@ApiOperation(value = "Comentar um livro")
	@RequestMapping(value = "/{id}/comentarios", method = RequestMethod.POST)
	public ResponseEntity<Void> adicionarComentario(@PathVariable("id")Long livroId, 
				@RequestBody Comentario comentario) {
		livrosService.salvarComentario(livroId, comentario);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).build(); 
	}
	@ApiOperation(value = "Lista comentarios de um livro")
	@RequestMapping(value = "/{id}/comentarios", method = RequestMethod.GET)
	public ResponseEntity<List<Comentario>> listarComentario(
			@PathVariable("id") Long livroId){
		List<Comentario> comentarios = livrosService.listarComentarios(livroId);
	
		return ResponseEntity.status(HttpStatus.OK).body(comentarios);
	}
}
