package org.serratec.ecommerce.api.domain;

import java.util.stream.Stream;

public enum StatusPedido {
	CONFIRMADO("C"), ENVIADO("E"), RECEBIDO("R"), CANCELADO("A");

	private String codigo;

	private StatusPedido(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	
	public static StatusPedido verifica(String codigo) throws IllegalArgumentException {
		return Stream.of(StatusPedido.values())
				.filter( s -> s.getCodigo().equals(codigo))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
