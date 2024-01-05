package dev.erikmota.desafiounikamain.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;

import java.io.Serializable;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActionsRequest {
    private List<Endereco> enderecoList = new ArrayList<>();
    private List<Monitorador> monitoradorList = new ArrayList<>();
    private static final ClientHttpConfiguration client = new ClientHttpConfiguration();
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public void atualizarListas(){
        monitoradorList = atualizar("http://localhost:8081/monitorador", Monitorador.class);
        enderecoList = atualizar("http://localhost:8081/endereco", Endereco.class);
        enderecoList.forEach(endereco ->
                monitoradorList.stream()
                        .filter(monitorador -> monitorador.getEnderecos().stream()
                                .anyMatch(enderecoMonitorador -> enderecoMonitorador.getCep().equals(endereco.getCep())))
                        .findFirst()
                        .ifPresent(endereco::setMonitorador)
        );
    }

    public <T> List<T> atualizar(String endereco, Class<T> classe) {
        List<T> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = client.requestGet(endereco);
            String json = response.body();
            lista = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, classe));

        } catch (Exception e) {
            System.out.println("Erro ao atualizar lista");
        }
        return lista;
    }

    public void cadastrar(String endereco, Object object) {
        try {
            System.out.println(mapper.writeValueAsString(object));
            HttpResponse<String> response = client.requestPost(endereco, mapper.writeValueAsString(object));
            System.out.println("Code: " + response.statusCode());
            System.out.println("Body: " + response.body());
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao realizar o cadastro");
        }
    }

    public void editar(String endereco, Object object) {
        try {
            HttpResponse<String> response = client.requestPut(endereco, mapper.writeValueAsString(object));
            System.out.println("Code: " + response.statusCode());
            System.out.println("Body: " + response.body());
            atualizarListas();

        } catch (Exception e) {
            System.out.println("Erro ao editar");
        }
        System.out.println("Editar");
    }

    public void excluir(String endereco) {
        try {
            HttpResponse<String> response = client.requestDelete(endereco);
            System.out.println("Code: " + response.statusCode());
            System.out.println("Body: " + response.body());
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao excluir");
        }
    }

    public <T> List<T> filtrar(String endereco, Class<T> classe) {
        List<T> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = client.requestGet(endereco);
            String json = response.body();
            lista = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, classe));
        } catch (Exception e) {
            System.out.println("Erro ao filtrar");
        }
        return lista;
    }

    public List<Monitorador> getMonitoradoresList(){
        return monitoradorList;
    }
    public List<Endereco> getEnderecoList(){
        return enderecoList;
    }

    public ActionsRequest(){
        atualizarListas();
    }
}
