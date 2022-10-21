package org.serratec.ecommerce.api.repository;

import org.serratec.ecommerce.api.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}

