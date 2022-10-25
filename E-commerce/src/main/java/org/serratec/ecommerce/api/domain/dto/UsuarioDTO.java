package org.serratec.ecommerce.api.domain.dto;

import java.util.HashSet;
import java.util.Set;

import org.serratec.ecommerce.api.domain.Perfil;
import org.serratec.ecommerce.api.domain.Usuario;
import org.serratec.ecommerce.api.domain.UsuarioPerfil;
import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private Set<Perfil> perfis;

	public UsuarioDTO() {
	}

	public UsuarioDTO(Long id, String nome, String email) {
		this.id = id;
		this.nome = nome;
		this.email = email;
	}

	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getIdUsuario();
		this.nome = usuario.getNomeUsuario();
		this.email = usuario.getEmail();
		this.perfis = new HashSet<>();
		for (UsuarioPerfil usuarioPerfil : usuario.getUsuarioPerfis()) {
			this.perfis.add(usuarioPerfil.getId().getPerfil());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
