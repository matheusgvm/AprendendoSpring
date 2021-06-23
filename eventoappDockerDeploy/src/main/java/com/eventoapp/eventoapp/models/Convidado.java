package com.eventoapp.eventoapp.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;

@Data   //Coloca os setters e getters sem poluir a classe
@Entity //indica que é uma tabela de um banco de dados
public class Convidado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long codigo;

    @NotEmpty
    private String rg;

    @NotEmpty
    private String nome;

    @ManyToOne // indica a relacao muitos pra um
    private Evento evento;

}
