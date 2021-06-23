package com.eventoapp.eventoapp.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class DetalhesEventoDTO {

    private long codigo;
    private String nome;
    private String local;
    private String data;
    private String horario;
    private Set<ConvidadoDTO> convidados;
}
