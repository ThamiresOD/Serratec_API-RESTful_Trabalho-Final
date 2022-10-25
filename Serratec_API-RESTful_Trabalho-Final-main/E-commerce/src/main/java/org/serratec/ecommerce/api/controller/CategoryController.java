package org.serratec.ecommerce.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.serratec.ecommerce.api.domain.Categoria;
import org.serratec.ecommerce.api.domain.dto.CategoriaDTO;
import org.serratec.ecommerce.api.domain.dto.CategoriaInserirDTO;
import org.serratec.ecommerce.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/categorias")
public class CategoryController {

	@Autowired
	private CategoriaRepository catRepo;

	@GetMapping
	@ApiOperation(value = "Listagem de todos as categorias")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorna a lista de categorias"),
			@ApiResponse(code = 401, message = "Erro de autenticação"),
			@ApiResponse(code = 403, message = "Não há permissão para acessar o recurso"),
			@ApiResponse(code = 404, message = "Recurso não encontrado"),
			@ApiResponse(code = 505, message = "Exceção interna da aplicação"), })
	public List<CategoriaDTO> getCategorias() {
		List<Categoria> categorias = catRepo.findAll();
		return CategoriaDTO.converter(categorias);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Busca de Categoria por ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorna Categoria especifica"),
			@ApiResponse(code = 401, message = "Erro de autenticação"),
			@ApiResponse(code = 403, message = "Não há permissão para acessar o recurso"),
			@ApiResponse(code = 404, message = "Recurso não encontrado"),
			@ApiResponse(code = 505, message = "Exceção interna da aplicação"), })
	public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
		Optional<Categoria> categoria = catRepo.findById(id);
		if (categoria.isPresent()) {
			return ResponseEntity.ok(new CategoriaDTO(categoria.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ApiOperation(value = "Inclusão de categorias")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Inclui categorias"),
			@ApiResponse(code = 401, message = "Erro de autenticação"),
			@ApiResponse(code = 403, message = "Não há permissão para acessar o recurso"),
			@ApiResponse(code = 404, message = "Recurso não encontrado"),
			@ApiResponse(code = 505, message = "Exceção interna da aplicação"), })
	public ResponseEntity<Categoria> inserir(@RequestBody Categoria categoria) {
		categoria = catRepo.save(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(categoria.getIdCategoria()).toUri();
		return ResponseEntity.created(uri).body(categoria);
	}

	@PutMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Update categoria específica")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorna alteração realizada"),
			@ApiResponse(code = 401, message = "Erro de autenticação"),
			@ApiResponse(code = 403, message = "Não há permissão para acessar o recurso"),
			@ApiResponse(code = 404, message = "Cliente não encontrado"),
			@ApiResponse(code = 505, message = "Exceção interna da aplicação"), })
	public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaInserirDTO form) {
		Optional<Categoria> optional = catRepo.findById(id);
		if (optional.isPresent()) {
			Categoria categoria = form.atualizar(id, catRepo);
			return ResponseEntity.ok(new CategoriaDTO(categoria));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Remoção de Categoria")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Categoria removida"),
			@ApiResponse(code = 401, message = "Erro de autenticação"),
			@ApiResponse(code = 403, message = "Não há permissão para acessar o recurso"),
			@ApiResponse(code = 404, message = "Cliente não encontrado"),
			@ApiResponse(code = 505, message = "Exceção interna da aplicação"), })
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		Optional<Categoria> categoriaBanco = catRepo.findById(id);
		if (!categoriaBanco.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		catRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}