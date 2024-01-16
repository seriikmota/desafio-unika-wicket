package dev.erikmota.desafiounikamain.modals;

import dev.erikmota.desafiounikamain.models.BooleanChoiceRenderer;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;


import java.util.*;

public class ModalMonitorador extends Panel {
    String tipoFormulario;
    List<Component> componentes = new ArrayList<>();
    TextField<String> cnpj, razao, inscricao, cpf, nome, rg;
    EmailTextField email;
    TextField<Date> data;
    DropDownChoice<TipoPessoa> tipo;
    RadioChoice<Boolean> ativo;
    Label cnpjLabel, razaoLabel, inscricaoLabel, cpfLabel, nomeLabel, rgLabel, dataLabel, tituloModal;
    FeedbackPanel feedback;
    private static final ActionsRequest request = ActionsRequest.getInstance();

    public ModalMonitorador(String id, ModalWindow modal, Monitorador monitorador){
        super(id);
        Monitorador m = new Monitorador(monitorador);
        if (monitorador.getId() == null) tipoFormulario = "Cadastrar";
        else tipoFormulario = "Editar";
        inicializarCampos();
        inicializarLabels();
        alterarTitulo(monitorador);
        componentes.forEach(c -> c.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

        Form<Monitorador> form = new Form<>("form", new CompoundPropertyModel<>(m));
        componentes.forEach(form::add);

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (tipoFormulario.equals("Editar"))
                    feedback.info(request.editar("monitorador/" + m.getId(), m));
                else
                    feedback.info(request.cadastrar("monitorador", m));
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
        cnpj = new TextField<>("cnpj");
        razao = new TextField<>("razao");
        inscricao = new TextField<>("inscricao");
        cpf = new TextField<>("cpf");
        nome = new TextField<>("nome");
        rg = new TextField<>("rg");
        data = new TextField<>("data");
        email = new EmailTextField("email");
        ativo = new RadioChoice<>("ativo", Arrays.asList(true, false), new BooleanChoiceRenderer()).setLabelPosition(AbstractChoice.LabelPosition.WRAP_AFTER);
        tipo = new DropDownChoice<>("tipo", Arrays.asList(TipoPessoa.values()));
        feedback = new FeedbackPanel("feedback");

        tipo.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                TipoPessoa tipoInput = tipo.getModelObject();
                if (tipoInput == TipoPessoa.FISICA)
                    camposFisica();
                else
                    camposJuridica();
                componentes.forEach(target::add);
            }
        });

        componentes.addAll(Arrays.asList(cnpj, razao, inscricao, cpf, nome, rg, data, email, ativo, tipo, feedback));

    }
    private void inicializarLabels(){
        cnpjLabel = new Label("cnpjLabel", "Cnpj");
        razaoLabel = new Label("razaoLabel", "Razao Social");
        inscricaoLabel = new Label("inscricaoLabel", "Inscricao Estadual");
        cpfLabel = new Label("cpfLabel", "Cpf");
        nomeLabel = new Label("nomeLabel", "Nome");
        rgLabel = new Label("rgLabel", "Rg");
        dataLabel = new Label("dataLabel", "Data de Nascimento/Abertura");
        componentes.addAll(Arrays.asList(cnpjLabel, razaoLabel, inscricaoLabel, cpfLabel, nomeLabel, rgLabel, dataLabel));
    }

    private void camposFisica(){
        cpf.setVisible(true); cpfLabel.setVisible(true);
        nome.setVisible(true); nomeLabel.setVisible(true);
        rg.setVisible(true); rgLabel.setVisible(true);
        cnpj.setVisible(false); cnpjLabel.setVisible(false);
        razao.setVisible(false); razaoLabel.setVisible(false);
        inscricao.setVisible(false); inscricaoLabel.setVisible(false);
        dataLabel.setDefaultModelObject("Data de Nascimento");
    }

    private void camposJuridica(){
        cpf.setVisible(false); cpfLabel.setVisible(false);
        nome.setVisible(false); nomeLabel.setVisible(false);
        rg.setVisible(false); rgLabel.setVisible(false);
        cnpj.setVisible(true); cnpjLabel.setVisible(true);
        razao.setVisible(true); razaoLabel.setVisible(true);
        inscricao.setVisible(true); inscricaoLabel.setVisible(true);
        dataLabel.setDefaultModelObject("Data de Abertura");
    }

    public void alterarTitulo(Monitorador m) {
        if (m.getId() != null) {
            tituloModal = new Label("titleModal", "Editar Monitorador");
            if (m.getTipo() == TipoPessoa.FISICA)
                camposFisica();
            else
                camposJuridica();
        } else {
            tituloModal = new Label("titleModal", "Cadastrar Monitorador");
        }
        add(tituloModal);
    }
}