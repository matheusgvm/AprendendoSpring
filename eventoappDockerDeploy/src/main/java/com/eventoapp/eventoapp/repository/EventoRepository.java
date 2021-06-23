package com.eventoapp.eventoapp.repository;

import com.eventoapp.eventoapp.models.Evento;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

//Usando o PagingAndSortingRepository para a paginacao, mas ele extende o CrudRepository tambem
// Faz a relacao entre o banco de dados e o programa
//<classe da entidade, tipo do identificador>
public interface EventoRepository extends PagingAndSortingRepository<Evento, Long>, QuerydslPredicateExecutor<Evento> {
    //metodos para buscas -> padrao de nomeacao
    Evento findByCodigo(long codigo); //busca o evento pelo codigo
}
