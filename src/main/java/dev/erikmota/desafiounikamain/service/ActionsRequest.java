package dev.erikmota.desafiounikamain.service;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erikmota.desafiounikamain.MonitoradorPage;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import org.apache.wicket.markup.html.link.ExternalLink;
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
        enderecoList.forEach(endereco -> {
            monitoradorList.stream()
                    .filter(monitorador -> monitorador.getEnderecos().stream()
                            .anyMatch(enderecoMonitorador -> enderecoMonitorador.getCep().equals(endereco.getCep())))
                    .findFirst()
                    .ifPresent(monitorador -> endereco.setMonitoradorId(monitorador.getId()));
        });
        Collections.sort(monitoradorList);
        Collections.sort(enderecoList);
    }

    public <T> List<T> obter(String path, Class<T> classe) {
        List<T> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = client.requestGet(endereco + path);
            String json = response.body();
            lista = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, classe));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar");
        }
        return lista;
    }

    public String cadastrar(String path, Object object) {
        String feedback = "asd";
        try {
            HttpResponse<String> response = client.requestPost(endereco + path, mapper.writeValueAsString(object));
            feedback = response.body();
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar");
        }
        return feedback;
    }

    public String editar(String path, Object object) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestPut(endereco + path, mapper.writeValueAsString(object));
            feedback = response.body();
            atualizarListas();

        } catch (Exception e) {
            System.out.println("Erro ao editar");
        }
        return feedback;
    }

    public String excluir(String path) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestDelete(endereco + path);
            feedback = response.body();
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao excluir");
        }
        return feedback;
    }

    public String importar(String path, File file) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestImportar(endereco + path, file.toPath());
            feedback = response.body();
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao importar");
        }
        return feedback;
    }

    public <T> List<T> filtrar(String path, Class<T> classe) {
        List<T> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = client.requestGet(endereco + path);
            String json = response.body();
            lista = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, classe));
        } catch (Exception e) {
            System.out.println("Erro ao filtrar");
        }
        return lista;
    }

    public Endereco buscarCep(String path) {
        Endereco e = new Endereco();
        try {
            HttpResponse<String> response = client.requestGet(endereco + path);
            String json = response.body();
            e = mapper.readValue(json, mapper.getTypeFactory().constructType(Endereco.class));
        } catch (Exception ex) {
            System.out.println("Erro ao buscar cep");
        }
        return e;
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
