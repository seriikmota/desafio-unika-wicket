package dev.erikmota.desafiounikamain.modals.endereco;

import dev.erikmota.desafiounikamain.EnderecoPage;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

public class ModalEndereco extends Panel {
    String tipoFormulario;
    TextField<String> cep, endereco, bairro, cidade, estado, telefone;
    private static final ActionsRequest request = new ActionsRequest();

    public ModalEndereco(String id, Endereco endereco){
        super(id);
        if (endereco.getId() == null)
            tipoFormulario = "Cadastrar";
        else
            tipoFormulario = "Editar";
        Endereco e = new Endereco(endereco);
        inicializarCampos();

        Form<Endereco> form = new Form<>("formEndereco", new CompoundPropertyModel<>(e)){
            @Override
            protected void onSubmit() {
                if (tipoFormulario.equals("Editar"))
                    request.editar("http://localhost:8081/endereco/" + e.getId(), e);
                else
                    request.cadastrar("http://localhost:8081/endereco", e);
                setResponsePage(EnderecoPage.class);
            }
        };
        add(form);
    }

    private void inicializarCampos(){
        cep = new TextField<>("cep");
        endereco = new TextField<>("endereco");
        bairro = new TextField<>("bairro");
        cidade = new TextField<>("cidade");
        estado = new TextField<>("estado");
        telefone = new TextField<>("telefone");
    }
}