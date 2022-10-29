package org.serratec.ecommerce.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.serratec.ecommerce.api.config.MailConfig;
import org.serratec.ecommerce.api.domain.Cliente;
import org.serratec.ecommerce.api.domain.ItemPedido;
import org.serratec.ecommerce.api.domain.Pedido;
import org.serratec.ecommerce.api.domain.Produto;
import org.serratec.ecommerce.api.domain.dto.ItemDTO;
import org.serratec.ecommerce.api.domain.dto.ItemInserirDTO;
import org.serratec.ecommerce.api.domain.dto.PedidoDTO;
import org.serratec.ecommerce.api.domain.dto.PedidoEmailDTO;
import org.serratec.ecommerce.api.domain.dto.PedidoInserirDTO;
import org.serratec.ecommerce.api.exception.ClienteNotFoundException;
import org.serratec.ecommerce.api.exception.PedidoNotFoundException;
import org.serratec.ecommerce.api.exception.ProdutoNotFoundException;
import org.serratec.ecommerce.api.repository.ClienteRepository;
import org.serratec.ecommerce.api.repository.ItemPedidoRepository;
import org.serratec.ecommerce.api.repository.PedidoRepository;
import org.serratec.ecommerce.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
	@Autowired
	ItemPedidoRepository itemPedidoRepo;
	@Autowired
	PedidoRepository pedidoRepository;
	@Autowired
	ProdutoRepository produtoRepository;
	@Autowired
	ClienteRepository clienteRepo;
	@Autowired
	MailConfig mailConfig;
	public List<PedidoDTO> findAll(){
		List<Pedido> pedidosDB = pedidoRepository.findAll();
		List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();
		
		for(Pedido pedidoDB : pedidosDB) {
			PedidoDTO pedidoDTO = new PedidoDTO(pedidoDB);
			List<ItemPedido> itemPedidosDB = itemPedidoRepo.findByPedido(pedidoDB);
			for (ItemPedido itemPedido : itemPedidosDB) {
				pedidoDTO.addItems(new ItemDTO(itemPedido));
			}
			pedidosDTO.add(pedidoDTO);
		}
		return pedidosDTO;
		
	}
	
	public PedidoDTO findById(Long id) {
		Optional<Pedido> pedidoDB = pedidoRepository.findById(id);
		
		PedidoDTO pedidoDTO = new PedidoDTO(pedidoDB.get());
		for (ItemPedido itemPedido : itemPedidoRepo.findByPedido(pedidoDB.get())) {
			pedidoDTO.addItems(new ItemDTO(itemPedido));
		}
		return pedidoDTO;
			}
	
	public PedidoDTO save(PedidoInserirDTO novoPedido) throws ClienteNotFoundException, ProdutoNotFoundException {
		Double valorTotal = 0.; 
		Double valorBruto,valorLiquido;
		PedidoDTO pedidoRes;
		List<ItemPedido> itemsDB = new ArrayList<>();
//		É procurado o cliente especificado no DB
		Optional<Cliente> cliente = clienteRepo.findById(novoPedido.getIdCliente());
		if(cliente.isEmpty()) {
			throw new ClienteNotFoundException(404, "Cliente não encontrado!");
		}
		
		//Cria items de acordo com os itemsInserirDTO passados, e adiciona o seu valor ao valor total
		for(ItemInserirDTO item : novoPedido.getItems()) {
			Optional<Produto> produto = produtoRepository.findById(item.getIdProduto());
			if (produto.isEmpty()) {
				throw new ProdutoNotFoundException(404, "Produto" + item.getIdProduto() + " não encontrado");
			}
			valorBruto = produto.get().getValorUnitarioProduto() * item.getQuantidade();
			valorLiquido = valorBruto - (valorBruto * (item.getPercentualDesconto()/100));
			valorTotal+= valorLiquido;
			
			ItemPedido novoItem = new ItemPedido(item, valorBruto, valorLiquido, produto.get());
			itemsDB.add(novoItem);
		}
		
		//Cria um pedido com esse valor total
		Pedido pedidoDB = new Pedido(novoPedido, valorTotal, cliente.get());
		pedidoRepository.save(pedidoDB);
		//Cria um DTO de resposta a partir do novo Pedido
		pedidoRes = new PedidoDTO(pedidoDB);
		
		//Para cada pedido novo criado, adicionamos o novo Pedido ao seu relacionamento e salvamos.
		//Também criamos DTOs de resposta pra esses items, e devolvemos junto com o PedidoDTO
		for(ItemPedido itemDB : itemsDB) {
			itemDB.setPedido(pedidoDB);
			itemPedidoRepo.save(itemDB);
			pedidoRes.addItems(new ItemDTO(itemDB));;
		}
		
		mailConfig.sendMail(cliente.get().getEmail(),"Pedido registrado com sucesso!",
				new PedidoEmailDTO(pedidoRes.getIdPedido(), pedidoRes.getDataPedido(), pedidoRes.getItems()).toString());
		return pedidoRes;
	}
	
	
	public PedidoDTO update(PedidoDTO novoPedido) throws PedidoNotFoundException{
		Double valorTotal = 0.;
		Optional<Pedido> pedidoDB = pedidoRepository.findById(novoPedido.getIdPedido());

		if(pedidoDB.isEmpty()) {
			throw new PedidoNotFoundException(404, "O pedido não foi encontrado!");
		} 
		for (ItemDTO item : novoPedido.getItems()) {
			valorTotal += item.getValorLiquido();
			itemPedidoRepo.save(new ItemPedido(item, pedidoDB.get()));
			
		}
		pedidoRepository.save(new Pedido(novoPedido, valorTotal));
		return findById(novoPedido.getIdPedido());
	}
	
	public void delete(Long id) throws PedidoNotFoundException{
		Optional<Pedido> pedidoDB = pedidoRepository.findById(id);
		if(pedidoDB.isEmpty()) {
			throw new PedidoNotFoundException(404, "Pedido não encontrado!");
		}
		for (ItemPedido item : itemPedidoRepo.findAllByPedido(pedidoDB.get())) {
			itemPedidoRepo.delete(item);
		}
		pedidoRepository.delete(pedidoDB.get());
		
	}

}
