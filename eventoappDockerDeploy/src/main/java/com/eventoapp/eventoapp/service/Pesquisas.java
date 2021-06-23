package com.eventoapp.eventoapp.service;

import com.eventoapp.eventoapp.DTO.ConvidadoDTO;
import com.eventoapp.eventoapp.DTO.DetalhesEventoDTO;
import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.models.QConvidado;
import com.eventoapp.eventoapp.models.QEvento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//por ser um component, a classe é instanciada automaticamente pelo spring
@Service
public class Pesquisas {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ConvidadoRepository convidadoRepository;

    private QEvento qEvento = QEvento.evento;
    private QConvidado qConvidado = QConvidado.convidado;


    public List getTodosEventos(){
        //Nao pode ficar no escopo de uma classe @Component
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        /*
        Long nElementos = query.fetchCount();
        List result = query.applyPagination(pageable, query).fetch();
        return new PageImpl<>(result, pageable, nElementos);*/

        return query.selectFrom(qEvento).fetch();
    }

    public List getEventosPorLocal(String local){
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        //retirar a tag version das dependencias do querydsl no pom.xml fez as pesquisas funcionarem corretamente
        return query.selectFrom(qEvento).where(qEvento.local.eq(local)).fetch();
    }

    public List getTodosHorarios(){
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        return query.select(qEvento.horario).from(qEvento).where(qEvento.horario.isNotNull()).fetch();
    }

    public DetalhesEventoDTO getDetalhesEvento(Long codigo){
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        Evento evento = query.selectFrom(qEvento).where(qEvento.codigo.eq(codigo)).fetchOne();
        DetalhesEventoDTO detalhesEventoDTO = new DetalhesEventoDTO();
        detalhesEventoDTO.setCodigo(evento.getCodigo());
        detalhesEventoDTO.setNome(evento.getNome());
        detalhesEventoDTO.setData(evento.getData());
        detalhesEventoDTO.setHorario(evento.getHorario());
        detalhesEventoDTO.setLocal(evento.getLocal());

        List<Convidado> convidados = query.selectFrom(qConvidado).where(qConvidado.evento.codigo.eq(evento.getCodigo())).fetch();
        Set<ConvidadoDTO> setConvidadoDTO = new HashSet();
        ConvidadoDTO convidadoDTO = new ConvidadoDTO();
        for(Convidado convidado : convidados){

            convidadoDTO.setCodigo(convidado.getCodigo());
            convidadoDTO.setNome(convidado.getNome());
            convidadoDTO.setRg(convidado.getRg());

            setConvidadoDTO.add(convidadoDTO);
        }

        detalhesEventoDTO.setConvidados(setConvidadoDTO);

        return detalhesEventoDTO;
    }

    public String getNomeEvento(Long codigo){
        /*JPAQueryFactory query = new JPAQueryFactory(entityManager);

        return query.selectFrom(qEvento).where(qEvento.codigo.eq(codigo)).fetchOne().getNome();*/

        return eventoRepository.findByCodigo(codigo).getNome();
    }

    /*public List teste(){
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        //retorna convidados
        //return query.selectFrom(qConvidado).join(qConvidado.evento, qEvento).on(qConvidado.evento.codigo.eq(qEvento.codigo)).fetch();
        return query.selectFrom(qEvento).leftJoin(qEvento.convidados, qConvidado).on(qConvidado.evento.codigo.eq(qEvento.codigo)).fetch();
    }*/

}
