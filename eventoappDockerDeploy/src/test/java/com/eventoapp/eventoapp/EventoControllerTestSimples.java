package com.eventoapp.eventoapp;


import com.eventoapp.eventoapp.controllers.EventoController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventoControllerTestSimples {

    @Test
    public void getNomeEventoTeste(){
        EventoController evento = new EventoController();
        String resposta = evento.unitTest();
        Assertions.assertEquals("Hello world", resposta);
    }

}
