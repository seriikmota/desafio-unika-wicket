package dev.erikmota.desafiounikamain.service;
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
            System.out.println("Erro ao atualizar");
        }
        return lista;
    }

    public String cadastrar(String endereco, Object object) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestPost(endereco, mapper.writeValueAsString(object));
            feedback = response.body();
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar");
        }
        return feedback;
    }

    public String editar(String endereco, Object object) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestPut(endereco, mapper.writeValueAsString(object));
            feedback = response.body();
            atualizarListas();

        } catch (Exception e) {
            System.out.println("Erro ao editar");
        }
        return feedback;
    }

    public String excluir(String endereco) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestDelete(endereco);
            feedback = response.body();
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao excluir");
        }
        return feedback;
    }

    public String importar(String endereco, File file) {
        String feedback = null;
        try {
            HttpResponse<String> response = client.requestImportar(endereco, file.toPath());
            feedback = response.body();
            atualizarListas();
        } catch (Exception e) {
            System.out.println("Erro ao importar");
            e.printStackTrace();
        }
        return feedback;
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
