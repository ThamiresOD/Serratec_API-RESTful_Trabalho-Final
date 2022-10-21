
--Clientes
INSERT INTO cliente(cli_tx_nome_completo, cli_tx_email, cli_tx_cpf, cli_tx_telefone, cli_dt_data_nascimento, end_cd_id) VALUES 
	('Vaninha', 'vaninha@gmail.com', '1234567978', '96875321845', '2000-05-12', 1),
	('Leonardo', 'l@gmail.com', '2334567989', '85875321879', '2000-06-20', 2),
	('Thata', 't@gmail.com', '1234567945', '32875321879', '2000-07-03', 3),
	('Breno', 'b@gmail.com', '1234567912', '73875321879', '2000-08-24', 4),
	('Marcelle', 'm@gmail.com', '9234567978', '64875321879', '2000-09-15', 4)
;

--Produto
INSERT INTO produto(prd_tx_nome, prd_tx_descricao, prd_int_quantidade_estoque, prd_dt_data_cadastro, prd_nm_valor_unitario, prd_imagem_produto, cat_cd_id) VALUES 
	('Boba feet', 'Boneco Boba feet', 10, '2022-06-04', 110, NULL, 1),
    ('Leia Organa', 'Boneco Leia Organa', 10, '2022-06-04', 110, NULL, 1),
 	('Yoda', 'Boneco Yoda', 10, '2022-06-04', 110, NULL, 1),
	('Batman', 'Boneco Batman', 10, '2022-06-04', 110, NULL, 2),
	('Lanterna Verde', 'Boneco Lanterna Verde', 10, '2022-06-04', 120, NULL, 2)   
;


