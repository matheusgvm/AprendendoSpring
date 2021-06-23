package com.eventoapp.eventoapp;

import com.eventoapp.eventoapp.controllers.EventoController;
import com.eventoapp.eventoapp.controllers.StorageController;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;
import com.eventoapp.eventoapp.service.Pesquisas;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EventoControllerTeste {

    @Autowired
    private EventoController eventoController;

    //Passar todas as dependencias que estao no EventoController, mas nao devem ser as dependencias reais -> por isso a notacao @mockbean
    @MockBean
    private EventoRepository eventoRepository;

    @MockBean
    private ConvidadoRepository convidadoRepository;

    @MockBean
    private Pesquisas pesquisas;

    //pq?
    @MockBean
    private StorageController storageController;

    @BeforeEach
    public void setup(){
        RestAssuredMockMvc.standaloneSetup(this.eventoController);
    }

    @Test
    public void sucess_testeGetNomeEvento(){
        Mockito.when(this.pesquisas.getNomeEvento(1L)).thenReturn("Cinema");

        RestAssuredMockMvc.given()
            .accept(ContentType.JSON)
            .when()
            .get("/getNomeEvento/{codigo}", 1L)
            .then()
            .statusCode(HttpStatus.OK.value());

    }

}
