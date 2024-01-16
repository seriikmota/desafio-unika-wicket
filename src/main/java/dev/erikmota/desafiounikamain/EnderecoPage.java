package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.modals.ModalEndereco;
import dev.erikmota.desafiounikamain.modals.ModalExcluir;
import dev.erikmota.desafiounikamain.modals.ModalImportar;
import dev.erikmota.desafiounikamain.modals.ModalMonitorador;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnderecoPage extends BasePage {
    private static final ActionsRequest request = ActionsRequest.getInstance();
    private String filtros = "endereco/filtrar?&text=&estado=&cidade=&monitorador=";
    public EnderecoPage() {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        ModalWindow modal = new ModalWindow("modal").setInitialHeight(500).setCssClassName("w_silver").setResizable(false);
        DropDownChoice<String> filtroEstado = new DropDownChoice<>("filtroEstado", Model.of(), getEstados());
        DropDownChoice<String> filtroCidade = new DropDownChoice<>("filtroCidade", Model.of(), getCidades());
        DropDownChoice<String> filtroMonitorador = new DropDownChoice<>("filtroMonitorador", Model.of(), getMonitoradores());
        TextField<String> pesquisar = new TextField<>("searchT", Model.of());


        ListView<Endereco> listView = new ListView<>("enderecoList", request.getEnderecoList()) {
            @Override
            protected void populateItem(ListItem<Endereco> item) {
                final Endereco endereco = item.getModelObject();
                item.add(new Label("eCep", endereco.getCep()));
                item.add(new Label("eEndereco", endereco.getEndereco()));
                item.add(new Label("eNumero", endereco.getNumero()));
                item.add(new Label("eBairro", endereco.getBairro()));
                item.add(new Label("eTelefone", endereco.getTelefone()));
                item.add(new Label("eCidade", endereco.getCidade()));
                item.add(new Label("eEstado", endereco.getEstado()));
                item.add(new Label("eMonitorador", endereco.getMonitoradorId()));
                item.add(new Label("ePrincipal", endereco.getPrincipal().equals(true) ? "Sim" : "Não"));
                item.add(new ExternalLink("relatorioInd", request.endereco + "endereco/relatorio?id=" + endereco.getId()));
                item.add(new AjaxLink<>("excluir", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modal.setInitialWidth(450).setInitialHeight(240);
                        modal.setContent(new ModalExcluir(modal.getContentId(), modal, "endereco/" + endereco.getId()));
                        modal.show(target);
                    }
                });
                item.add(new AjaxLink<>("editar", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modal.setInitialWidth(600).setInitialHeight(500);
                        modal.setContent(new ModalEndereco(modal.getContentId(), modal, endereco));
                        modal.show(target);
                    }
                });
            }
        };
        listView.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);

        add(filtroCidade.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valorSelecionado = filtroCidade.getModelObject();
                if (valorSelecionado != null){
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("cidade=") ? "cidade=" + valorSelecionado : s)
                            .collect(Collectors.joining("&"));
                }
                else{
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("cidade=") ? "cidade=" : s)
                            .collect(Collectors.joining("&"));
                }
                List<Endereco> e = request.obter(filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                target.add(container);
            }
        }));

        add(filtroEstado.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valorSelecionado = filtroEstado.getModelObject();
                if (valorSelecionado != null){
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("estado=") ? "estado=" + valorSelecionado : s)
                            .collect(Collectors.joining("&"));
                }
                else{
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("estado=") ? "estado=" : s)
                            .collect(Collectors.joining("&"));
                }
                List<Endereco> e = request.obter(filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                target.add(container);
            }
        }));

        add(filtroMonitorador.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valorSelecionado = filtroMonitorador.getModelObject();
                if (valorSelecionado != null){
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("monitorador=") ? "monitorador=" + valorSelecionado : s)
                            .collect(Collectors.joining("&"));
                }
                else{
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("monitorador=") ? "monitorador=" : s)
                            .collect(Collectors.joining("&"));
                }
                List<Endereco> e = request.obter(filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                target.add(container);
            }
        }));

        add(pesquisar.add(new OnChangeAjaxBehavior() {
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
                List<Endereco> e = request.obter(filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                target.add(container);
            }
        }));

        add(new Link<Void>("hrefDashboard") {
            @Override
            public void onClick() {
                setResponsePage(DashboardPage.class);
            }
        });

        add(new Link<Void>("hrefMonitorador") {
            @Override
            public void onClick() {
                setResponsePage(MonitoradorPage.class);
            }
        });

        add(new AjaxLink<Void>("cadastrar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setInitialWidth(600).setInitialHeight(500);
                modal.setContent(new ModalEndereco(modal.getContentId(), modal, new Endereco()));
                modal.show(target);
            }
        });

        add(new AjaxLink<Void>("importar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setInitialWidth(500).setInitialHeight(370);
                modal.setContent(new ModalImportar(modal.getContentId(), modal, request.endereco + "endereco/"));
                modal.show(target);
            }
        });

        add(new ExternalLink("relatorio", request.endereco + "endereco/relatorio"));

        modal.setWindowClosedCallback((ModalWindow.WindowClosedCallback) target -> {
            List<Endereco> e = request.getEnderecoList();
            listView.setList(e);
            target.add(container);
        });

        container.add(listView);
        add(filtroEstado, filtroCidade, filtroMonitorador, pesquisar, container, modal);
    }

    private List<String> getEstados(){
        return request.getEnderecoList()
                .stream()
                .map(Endereco::getEstado)
                .distinct()
                .collect(Collectors.toList());
    }
    private List<String> getCidades() {
        return request.getEnderecoList()
                .stream()
                .map(Endereco::getCidade)
                .distinct()
                .collect(Collectors.toList());
    }
    private List<String> getMonitoradores() {
        return request.getEnderecoList()
                .stream()
                .map(Endereco::getCidade)
                .distinct()
                .collect(Collectors.toList());
    }
}

