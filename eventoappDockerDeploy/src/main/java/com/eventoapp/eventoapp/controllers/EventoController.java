package com.eventoapp.eventoapp.controllers;

import com.eventoapp.eventoapp.DTO.DetalhesEventoDTO;
import com.eventoapp.eventoapp.service.Pesquisas;
import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.util.List;
import java.util.Set;

//@Controller -> indica que é uma classe de controle
//@RestController -> indica que é uma classe de controle com caracteristicas REST -> incoporar a notacao @ResponseBody em todos os metodos
@Controller
public class EventoController {

    @Autowired //injecao de dependencias -> cria uma nova instancia da interface automaticamente(administrada pelo spring)
    private EventoRepository eventoRepository;

    @Autowired
    private ConvidadoRepository convidadoRepository;

    @Autowired
    private Pesquisas pesquisas;

    //retorna o formEvento.html
    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET) //permite definir uma rota
    public String form(){
        return "evento/formEvento";
    }

    //recebe do html o objeto evento já montado e salva no bd
    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
    public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return  "redirect:/cadastrarEvento";
        }
        eventoRepository.save(evento);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");

        return "redirect:/cadastrarEvento";
    }

    //Esse metodo esta sendo paginado -> para paginar é so mudar o metodo de extensao no repositorio e fazer igual a esse metodo
    //Para customizar a paginacao -> https://www.youtube.com/watch?v=bYv-BdGrAg0&ab_channel=DevDojo
    @RequestMapping("/eventos")
    public ModelAndView listaEventos(Pageable pageable){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = eventoRepository.findAll(pageable);
        mv.addObject("eventos", eventos);

        return mv;
    }

    //@pathVariabel indica que o valor da variável virá de uma informação da rota
    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo){
        Evento evento = eventoRepository.findByCodigo(codigo);

        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        mv.addObject("evento", evento);

        Iterable<Convidado> convidados = convidadoRepository.findByEvento(evento);
        mv.addObject("convidados", convidados);

        return mv;
    }

    @RequestMapping("/deletarEvento")
    public String deletarEvento(long codigo){
        Evento evento = eventoRepository.findByCodigo(codigo);
        eventoRepository.delete(evento);

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
    public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return  "redirect:/{codigo}";
        }
        Evento evento = eventoRepository.findByCodigo(codigo);
        convidado.setEvento(evento);
        convidadoRepository.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");

        return "redirect:/{codigo}";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(long codigo){
        Convidado convidado = convidadoRepository.findByCodigo(codigo);
        convidadoRepository.delete(convidado);

        //Acha o codigo do evento para retornar o direcionamento
        Evento evento = convidado.getEvento();
        long codigoLong = evento.getCodigo();
        String codigoString = "" + codigoLong;

        return "redirect:/" + codigoString;
    }

    @Cacheable("eventos")   //-> cacheia o metodo para a regiao de memoria denominadas "eventos"
    @ResponseBody //-> indica para o controller para retornar o objeto em formato json automaticamente
    @RequestMapping(value = "/getTodosEventos", method = RequestMethod.GET)
    public List getTodosEventos(){
        //Por ser um component, a classe pesquisa é instanciada automaticamente pelo spring
        List<Evento> listResultQuery = pesquisas.getTodosEventos();

        System.out.println("Sem cache");

        return listResultQuery;
    }

    @ResponseBody
    @RequestMapping(value = "/getEventosPorLocal/{local}", method = RequestMethod.GET)
    public List getEventosPorLocal(@PathVariable("local") String local){
        return pesquisas.getEventosPorLocal(local);
    }

    @ResponseBody
    @RequestMapping(value = "/todosHorarios", method = RequestMethod.GET)
    public List getTodosHorarios(){
        return pesquisas.getTodosHorarios();
    }

    @CacheEvict("eventos") //-> indica que quando esse metodo é chamado a cache de eventos deve ser invalidada
    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public String cancelaCache(){
        return "Cache cancelado";
    }

    @ResponseBody
    @GetMapping(value = "/getDetalhesEvento/{codigo}")
    public DetalhesEventoDTO getDetalhesEvento(@PathVariable("codigo") Long codigo){
        return pesquisas.getDetalhesEvento(codigo);
    }

    @ResponseBody
    @GetMapping(value = "/getNomeEvento/{codigo}")
    public String getNomeEvento(@PathVariable("codigo") Long codigo){
        return pesquisas.getNomeEvento(codigo);
    }

    @ResponseBody
    @GetMapping("/unitTest")
    public String unitTest(){
        return "Hello world";
    }

    /*@ResponseBody
    @GetMapping(value = "/teste")
    public List teste(){
        return pesquisas.teste();
    }*/
}
