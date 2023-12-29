package dev.erikmota.desafiounikamain.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ActionsRequest {
    public static ClientHttpConfiguration client = new ClientHttpConfiguration();
    public ObjectMapper mapper = new ObjectMapper();

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
            /*HttpResponse<String> response = client.requestPost(endereco, mapper.writeValueAsString(object));
            System.out.println("Code: " + response.statusCode() + "Body: " + response.body());*/

        } catch (Exception e) {
            System.out.println("Erro ao realizar o cadastro");
        }
    }

    /*public void editar(String endereco, Object object) {
        try {
            HttpResponse<String> response = client.requestPut(endereco, mapper.writeValueAsString(object));
            System.out.println("Code: " + response.statusCode() + "Body: " + response.body());

        } catch (Exception e) {
            System.out.println("Erro ao editar");
        }
    }*/

    /*public void excluir(String endereco) {
        try {
            HttpResponse<String> response = client.requestDelete(endereco);
            System.out.println("Code: " + response.statusCode() + "Body: " + response.body());

        } catch (Exception e) {
            System.out.println("Erro ao excluir");
        }
    }*/
}
