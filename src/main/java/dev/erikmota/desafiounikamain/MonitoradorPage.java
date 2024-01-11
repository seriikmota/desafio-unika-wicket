package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.modals.ModalImportar;
import dev.erikmota.desafiounikamain.modals.ModalMonitorador;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MonitoradorPage extends BasePage {
    List<Monitorador> monitoradorList;
    private static final ActionsRequest request = new ActionsRequest();
    final String endereco = "http://localhost:8081/monitorador";
    private String filtros = "/filtrar?&text=&ativo=&tipo=";
    public MonitoradorPage(){
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        monitoradorList = request.atualizar(endereco, Monitorador.class);
        Collections.sort(monitoradorList);
        ModalWindow modal = new ModalWindow("modal").setInitialHeight(500).setCssClassName("w_silver").setResizable(false);
        DropDownChoice<TipoPessoa> filtroPessoa = new DropDownChoice<>("filtroPessoa", Model.of(), Arrays.asList(TipoPessoa.values()));
        DropDownChoice<String> filtroAtivo = new DropDownChoice<>("filtroAtivo", Model.of(), Arrays.asList("Sim", "Nao"));
        TextField<String> pesquisar = new TextField<>("searchT", Model.of());

        ListView<Monitorador> listView = new ListView<>("monitoradorList", monitoradorList) {
            @Override
            protected void populateItem(ListItem<Monitorador> item) {
                final Monitorador monitorador = item.getModelObject();
                item.add(new Label("mTipo", monitorador.getTipo()));
                item.add(new Label("mCnpj", monitorador.getCnpj()));
                item.add(new Label("mRazao", monitorador.getRazao()));
                item.add(new Label("mCpf", monitorador.getCpf()));
                item.add(new Label("mNome", monitorador.getNome()));
                item.add(new Label("mQuantidadeEndereco", monitorador.getEnderecos().size()));
                item.add(new Label("mAtivo", monitorador.getAtivo().equals(true) ? "Sim" : "Não"));
                item.add(new AjaxLink<>("excluir", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        request.excluir(endereco + "/" + monitorador.getId());
                        setResponsePage(MonitoradorPage.class);
                    }
                });
                item.add(new AjaxLink<>("editar", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modal.setInitialWidth(600).setInitialHeight(500);
                        modal.setContent(new ModalMonitorador(modal.getContentId(), monitorador));
                        modal.show(target);
                    }
                });
            }
        };
        listView.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);

        filtroAtivo.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valorSelecionado = filtroAtivo.getModelObject();
                if (valorSelecionado != null){
                    boolean ativo = valorSelecionado.equals("Sim");
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("ativo=") ? "ativo=" + ativo : s)
                            .collect(Collectors.joining("&"));
                }
                else{
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("ativo=") ? "ativo=" : s)
                            .collect(Collectors.joining("&"));
                }
                List<Monitorador> m = request.atualizar(endereco + filtros, Monitorador.class);
                Collections.sort(m);
                listView.setList(m);
                target.add(container);
            }
        });

        filtroPessoa.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                TipoPessoa valorSelecionado = filtroPessoa.getModelObject();
                if (valorSelecionado != null){
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("tipo=") ? "tipo=" + valorSelecionado : s)
                            .collect(Collectors.joining("&"));
                }
                else{
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("tipo=") ? "tipo=" : s)
                            .collect(Collectors.joining("&"));
                }
                List<Monitorador> m = request.atualizar(endereco + filtros, Monitorador.class);
                Collections.sort(m);
                listView.setList(m);
                target.add(container);
            }
        });

        pesquisar.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valor = pesquisar.getModelObject();
                if (valor != null){
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("text=") ? "text=" + valor : s)
                            .collect(Collectors.joining("&"));
                }
                else{
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("text=") ? "text=" : s)
                            .collect(Collectors.joining("&"));
                }
                filtros = filtros.replace(" ", "+");
                List<Monitorador> m = request.atualizar(endereco + filtros, Monitorador.class);
                Collections.sort(m);
                listView.setList(m);
                target.add(container);
            }
        });

        add(new AjaxLink<Void>("cadastrar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setInitialWidth(600).setInitialHeight(500);
                modal.setContent(new ModalMonitorador(modal.getContentId(), new Monitorador()));
                modal.show(target);
            }
        });

        add(new AjaxLink<Void>("importar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setInitialWidth(500).setInitialHeight(340);
                modal.setContent(new ModalImportar(modal.getContentId()));
                modal.show(target);
            }
        });

        add(new Link<Void>("hrefDashboard") {
            @Override
            public void onClick() {
                setResponsePage(DashboardPage.class);
            }
        });

        add(new Link<Void>("hrefEndereco") {
            @Override
            public void onClick() {
                setResponsePage(EnderecoPage.class);
            }
        });

        container.add(listView);
        add(filtroAtivo);
        add(filtroPessoa);
        add(modal);
        add(pesquisar);
        add(container);


    }
}