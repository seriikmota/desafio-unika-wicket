package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ActionsRequest;

import org.apache.wicket.markup.html.WebPage;

import java.util.List;

public class BasePage extends WebPage {
    public List<Endereco> enderecoList;
    public List<Monitorador> monitoradorList;

    public ActionsRequest request = new ActionsRequest();

    public void atualizarListas(){
        monitoradorList = request.atualizar("http://localhost:8081/monitorador", Monitorador.class);
        /*enderecoList = request.atualizar("http://localhost:8081/endereco", Endereco.class);
        enderecoList.forEach(endereco ->
                monitoradorList.stream()
                        .filter(monitorador -> monitorador.getEnderecos().stream()
                                .anyMatch(enderecoMonitorador -> enderecoMonitorador.getCep().equals(endereco.getCep())))
                        .findFirst()
                        .ifPresent(endereco::setMonitorador)
        );*/
    }
}
