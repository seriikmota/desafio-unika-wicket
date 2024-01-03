package dev.erikmota.desafiounikamain.modals;

import dev.erikmota.desafiounikamain.MonitoradorPage;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.DateValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ModalMonitorador extends Panel {
    String tipoFormulario;
    List<Component> componentes = new ArrayList<>();
    TextField<String> cnpj, razao, inscricao, cpf, nome, rg;
    EmailTextField email;
    TextField<Date> data;
    DropDownChoice<TipoPessoa> tipo;
    RadioChoice<Boolean> ativo;
    Label cnpjLabel, razaoLabel, inscricaoLabel, cpfLabel, nomeLabel, rgLabel, dataLabel, tituloModal, tituloBotao;
    private static final ActionsRequest request = new ActionsRequest();

    public ModalMonitorador(String id, Monitorador monitorador){
        super(id);
        Monitorador m = new Monitorador(monitorador);
        if (monitorador.getId() == null)
            tipoFormulario = "Cadastrar";
        else
            tipoFormulario = "Editar";

        inicializarCampos();
        inicializarLabels();
        alterarTitulos(monitorador);
        componentes.forEach(c -> c.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

        tipo.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                TipoPessoa tipoPessoa = tipo.getModelObject();
                if (tipoPessoa == TipoPessoa.FISICA)
                    camposFisica();
                else
                    camposJuridica();
                componentes.forEach(target::add);
            }
        });

        Form<Monitorador> form = new Form<>("formMonitorador", new CompoundPropertyModel<>(m)){
            @Override
            protected void onSubmit() {
                if (tipoFormulario.equals("Editar"))
                    request.editar("http://localhost:8081/monitorador/" + m.getId(), m);
                else
                    request.cadastrar("http://localhost:8081/monitorador", m);
                setResponsePage(MonitoradorPage.class);
            }
        };
        componentes.forEach(form::add);
        form.add(tituloBotao);

        form.add(new FeedbackPanel("feedback1"));
        add(form);
    }

    private void inicializarCampos(){
        cnpj = new TextField<>("cnpj");
        razao = new TextField<>("razaoSocial");
        inscricao = new TextField<>("inscricaoEstadual");
        cpf = new TextField<>("cpf");
        nome = new TextField<>("nome");
        rg = new TextField<>("rg");
        data = new TextField<>("data");
        email = new EmailTextField("email");
        ativo = new RadioChoice<>("ativo", Arrays.asList(true, false)).setLabelPosition(AbstractChoice.LabelPosition.WRAP_AFTER);
        tipo = new DropDownChoice<>("tipoPessoa", Arrays.asList(TipoPessoa.values()));

        /*LocalDate localDate = LocalDate.now().minusYears(18);
        Date date = Date.from(localDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
        data.add(DateValidator.maximum(date, "dd/mm/yyyy"));*/

        componentes.addAll(Arrays.asList(cnpj, razao, inscricao, cpf, nome, rg, data, email, ativo, tipo));

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

    public void alterarTitulos(Monitorador m){
        if (m.getId() != null){
            tituloModal = new Label("titleModal", "Editar Monitorador");
            tituloBotao = new Label("titleButton","Editar");
            if(m.getTipoPessoa() == TipoPessoa.FISICA)
                camposFisica();
            else

                camposJuridica();
        }
        else {
            tituloModal = new Label("titleModal", "Cadastrar Monitorador");
            tituloBotao = new Label("titleButton","Cadastrar");
        }
        add(tituloModal);
    }

}