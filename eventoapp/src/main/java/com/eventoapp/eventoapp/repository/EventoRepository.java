package com.eventoapp.eventoapp.repository;

import com.eventoapp.eventoapp.models.Evento;
import org.springframework.data.repository.CrudRepository;

// Faz a relacao entre o banco de dados e o programa
//<classe da entidade, tipo do identificador>
public interface EventoRepository extends CrudRepository<Evento, String>{
    //metodos para buscas -> padrao de nomeacao
    Evento findByCodigo(long codigo); //busca o evento pelo codigo
}
