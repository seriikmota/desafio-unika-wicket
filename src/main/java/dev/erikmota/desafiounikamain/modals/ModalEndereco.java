package dev.erikmota.desafiounikamain.modals;

import dev.erikmota.desafiounikamain.models.*;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModalEndereco extends Panel {
    List<Component> componentes = new ArrayList<>();
    TextField<String> cep, endereco, numero, bairro, telefone, cidade;
    DropDownChoice<String> estado;
    RadioChoice<Boolean> principal;
    MonitoradorDropDownChoice monitorador;
    FeedbackPanel feedback;
    private static final ActionsRequest request = ActionsRequest.getInstance();

    public ModalEndereco(String id, ModalWindow modal, Endereco endereco){
        super(id);
        String tipoFormulario = (endereco.getId() == null) ? "Cadastrar" : "Editar";
        Endereco e = new Endereco(endereco);
        inicializarCampos();
        alterarTitulo(endereco);
        componentes.forEach(c -> c.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

        Form<Endereco> form = new Form<>("form", new CompoundPropertyModel<>(e));
        componentes.forEach(form::add);

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (monitorador.getModelObject() != null){
                    Long idMonitorador = monitorador.getModelObject().getId();
                    if (tipoFormulario.equals("Editar"))
                        feedback.info(request.editar("endereco/" + e.getId() + "?idMonitorador=" + idMonitorador, e));
                    else
                        feedback.info(request.cadastrar("endereco?idMonitorador=" + idMonitorador, e));
                }
                else {
                    feedback.info("O monitorador deve ser definido!");
                }
                target.add(feedback);
            }
        });

        form.add(new AjaxLink<Void>("close") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.close(target);
            }
        });

        add(form);
    }

    private void inicializarCampos(){
        List<String> estados = new ArrayList<>(Arrays.asList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
                                                             "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"));

        cep = new TextField<>("cep");
        endereco = new TextField<>("endereco");
        numero = new TextField<>("numero");
        bairro = new TextField<>("bairro");
        estado = new DropDownChoice<>("estado", estados);
        monitorador = new MonitoradorDropDownChoice("monitorador", Model.of(new Monitorador()), request.getMonitoradoresList());
        cidade = new TextField<>("cidade");
        telefone = new TextField<>("telefone");
        principal = new RadioChoice<>("principal", Arrays.asList(true, false), new BooleanChoiceRenderer()).setLabelPosition(AbstractChoice.LabelPosition.WRAP_AFTER);
        feedback = new FeedbackPanel("feedback");

        telefone.add(new Mask("(99) 99999-9999"));
        cep.add(new Mask("99999-999"));

        cep.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (cep.getModelObject() != null){
                    String buscaCep = cep.getModelObject().replaceAll("[^0-9]", "");
                    if (buscaCep.length() == 8){
                        Endereco e = request.buscarCep("endereco/cep/" + buscaCep);
                        endereco.setModelObject(e.getEndereco());
                        bairro.setModelObject(e.getBairro());
                        cidade.setModelObject(e.getCidade());
                        estado.setModelObject(e.getEstado());
                        target.add(endereco, bairro, cidade, estado);
                    }
                }
            }
        });

        componentes.addAll(Arrays.asList(cep, endereco, numero, bairro, cidade, estado, telefone, monitorador, principal, feedback));


    }

    public void alterarTitulo(Endereco e) {
        Label tituloModal;
        if (e.getId() != null) {
            tituloModal = new Label("titleModal", "Editar Endereco");
        } else {
            tituloModal = new Label("titleModal", "Cadastrar Endereco");
        }
        add(tituloModal);
    }
}
