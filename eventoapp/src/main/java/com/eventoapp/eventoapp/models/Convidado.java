package com.eventoapp.eventoapp.models;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

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

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
