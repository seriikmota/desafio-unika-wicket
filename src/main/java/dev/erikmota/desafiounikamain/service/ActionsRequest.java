package dev.erikmota.desafiounikamain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import org.apache.wicket.util.file.File;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionsRequest {
    private static ActionsRequest instance;
    private List<Endereco> enderecoList = new ArrayList<>();
    private List<Monitorador> monitoradorList = new ArrayList<>();
    private static final ClientHttpConfiguration client = new ClientHttpConfiguration();
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    public final String endereco = "http://localhost:8081/";

    private ActionsRequest(){}

    public static synchronized ActionsRequest getInstance() {
        if (instance == null)
            instance = new ActionsRequest();
        return instance;
    }

    public void atualizarListas(){
        monitoradorList = obter("monitorador", Monitorador.class);
        enderecoList = obter("endereco", Endereco.class);
        enderecoList.forEach(endereco ->
                monitoradorList.stream()
                        .filter(monitorador -> monitorador.getEnderecos().stream()
                                .anyMatch(enderecoMonitorador -> enderecoMonitorador.getCep().equals(endereco.getCep())))
                        .findFirst()
                        .ifPresent(endereco::setMonitorador)
        );
        Collections.sort(monitoradorList);
        Collections.sort(enderecoList);
    }

    public <T> List<T> obter(String path, Class<T> classe) {
        try {
            HttpResponse<String> response = client.requestGet(endereco + path);
            String json = response.body();
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, classe));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String cadastrar(String path, Object object) {
        try {
            HttpResponse<String> response = client.requestPost(endereco + path, mapper.writeValueAsString(object));
            atualizarListas();
            return response.statusCode() + ": " + response.body();
        } catch (Exception e) {
            return "Erro ao cadastrar";
        }
    }

    public String editar(String path, Object object) {
        try {
            HttpResponse<String> response = client.requestPut(endereco + path, mapper.writeValueAsString(object));
            atualizarListas();
            return response.statusCode() + ": " + response.body();
        } catch (Exception e) {
            return "Erro ao editar";
        }
    }

    public String excluir(String path) {
        try {
            HttpResponse<String> response = client.requestDelete(endereco + path);
            atualizarListas();
            return response.statusCode() + ": " + response.body();
        } catch (Exception e) {
            return "Erro ao excluir";
        }
    }

    public String importar(String path, File file) {
        try {
            HttpResponse<String> response = client.requestImportar(endereco + path, file.toPath());
            atualizarListas();
            return response.statusCode() + ": " + response.body();
        } catch (Exception e) {
            return "Erro ao importar";
        }
    }

    public Endereco buscarCep(String path) {
        try {
            HttpResponse<String> response = client.requestGet(endereco + path);
            String json = response.body();
            return mapper.readValue(json, mapper.getTypeFactory().constructType(Endereco.class));
        } catch (Exception ex) {
            return new Endereco();
        }
    }

    public List<Monitorador> getMonitoradoresList(){
        atualizarListas();
        if (monitoradorList.isEmpty())
            return new ArrayList<>();
        return monitoradorList;
    }
    public List<Endereco> getEnderecoList(){
        atualizarListas();
        if (enderecoList.isEmpty())
            return new ArrayList<>();
        return enderecoList;
    }
}
