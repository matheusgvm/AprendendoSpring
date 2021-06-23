package com.eventoapp.eventoapp.repository;

import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ConvidadoRepository extends PagingAndSortingRepository<Convidado, Long>, QuerydslPredicateExecutor<Convidado> {

    Iterable<Convidado> findByEvento(Evento evento);
    Convidado findByCodigo(long codigo);

}
