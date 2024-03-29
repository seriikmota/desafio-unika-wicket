package dev.erikmota.desafiounika;

import dev.erikmota.desafiounika.modals.ModalImportar;
import dev.erikmota.desafiounika.modals.ModalMonitorador;
import dev.erikmota.desafiounika.modals.ModalExcluir;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MonitoradorPage extends WebPage {
    private static final ActionsRequest request = ActionsRequest.getInstance();
    private String filtros = "?&text=&ativo=&tipo=";
    ExternalLink linkPdf = new ExternalLink("relatorioPdf", request.endereco + "monitorador/relatorioPdf" + filtros);
    ExternalLink linkExcel = new ExternalLink("relatorioExcel", request.endereco + "monitorador/relatorioExcel" + filtros);

    public MonitoradorPage(){
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        ModalWindow modal = new ModalWindow("modal").setInitialHeight(500).setCssClassName("w_silver").setResizable(false);
        DropDownChoice<TipoPessoa> filtroPessoa = new DropDownChoice<>("filtroPessoa", Model.of(), Arrays.asList(TipoPessoa.values()));
        DropDownChoice<String> filtroAtivo = new DropDownChoice<>("filtroAtivo", Model.of(), Arrays.asList("Sim", "Nao"));
        TextField<String> pesquisar = new TextField<>("searchT", Model.of());

        PageableListView<Monitorador> listView = new PageableListView<>("monitoradorList", request.getMonitoradoresList(), 15) {
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
                item.add(new ExternalLink("relatorioPdfInd", request.endereco + "monitorador/relatorioPdf?id=" + monitorador.getId()));
                item.add(new ExternalLink("relatorioExInd", request.endereco + "monitorador/relatorioExcel?id=" + monitorador.getId()));
                item.add(new AjaxLink<>("excluir", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modal.setInitialWidth(450).setInitialHeight(300);
                        modal.setContent(new ModalExcluir(modal.getContentId(), modal, "monitorador/" + monitorador.getId()));
                        modal.show(target);
                    }
                });
                item.add(new AjaxLink<>("editar", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modal.setInitialWidth(600).setInitialHeight(500);
                        modal.setContent(new ModalMonitorador(modal.getContentId(), modal,  monitorador));
                        modal.show(target);
                    }
                });
            }
        };
        listView.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);

        linkPdf.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        linkExcel.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(linkPdf, linkExcel);

        add(filtroAtivo.add(new OnChangeAjaxBehavior() {
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
                List<Monitorador> m = request.obter("monitorador/filtrar" + filtros, Monitorador.class);
                Collections.sort(m);
                listView.setList(m);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
            }
        }));

        add(filtroPessoa.add(new OnChangeAjaxBehavior() {
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
                List<Monitorador> m = request.obter("monitorador/filtrar" + filtros, Monitorador.class);
                Collections.sort(m);
                listView.setList(m);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
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
                filtros = filtros.replace(" ", "%20");
                List<Monitorador> m = request.obter("monitorador/filtrar" + filtros, Monitorador.class);
                Collections.sort(m);
                listView.setList(m);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
            }
        }));

        add(new AjaxLink<Void>("cadastrar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setInitialWidth(600).setInitialHeight(500);
                modal.setContent(new ModalMonitorador(modal.getContentId(), modal, new Monitorador()));
                modal.show(target);
            }
        });

        add(new AjaxLink<Void>("importar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setInitialWidth(500).setInitialHeight(370);
                modal.setContent(new ModalImportar(modal.getContentId(), modal, "monitorador/importar"));
                modal.show(target);
            }
        });

        modal.setWindowClosedCallback((ModalWindow.WindowClosedCallback) target -> {
            listView.setList(request.getMonitoradoresList());
            target.add(container);
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

        container.add(new PagingNavigator("navigator", listView));
        container.add(listView);
        add(modal);
        add(container);
    }

    private void atualizarRelatorios(){
        String urlPdf = request.endereco + "monitorador/relatorioPdf" + filtros;
        String urlExcel = request.endereco + "monitorador/relatorioExcel" + filtros;
        linkPdf.setDefaultModel(Model.of(urlPdf));
        linkExcel.setDefaultModel(Model.of(urlExcel));
    }
}