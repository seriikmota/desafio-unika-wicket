package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.modals.ModalEndereco;
import dev.erikmota.desafiounikamain.modals.ModalExcluir;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.MonitoradorDropDownChoice;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;

import java.util.*;
import java.util.stream.Collectors;

public class EnderecoPage extends WebPage {
    private static final ActionsRequest request = ActionsRequest.getInstance();
    private String filtros = "?&text=&estado=&cidade=&monitorador=";
    ExternalLink linkPdf = new ExternalLink("relatorioPdf", request.endereco + "endereco/relatorioPdf" + filtros);
    ExternalLink linkExcel = new ExternalLink("relatorioExcel", request.endereco + "endereco/relatorioExcel" + filtros);

    public EnderecoPage() {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        ModalWindow modal = new ModalWindow("modal").setInitialHeight(500).setCssClassName("w_silver").setResizable(false);
        DropDownChoice<String> filtroEstado = new DropDownChoice<>("filtroEstado", Model.of(), getEstados());
        DropDownChoice<String> filtroCidade = new DropDownChoice<>("filtroCidade", Model.of(), getCidades());
        MonitoradorDropDownChoice filtroMonitorador = new MonitoradorDropDownChoice("filtroMonitorador", Model.of(), getMonitoradores());
        TextField<String> pesquisar = new TextField<>("searchT", Model.of());


        PageableListView<Endereco> listView = new PageableListView<>("enderecoList", request.getEnderecoList(), 15) {
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
                item.add(new Label("eMonitorador", getNomeMonitorador(endereco.getCep())));
                item.add(new Label("ePrincipal", endereco.getPrincipal().equals(true) ? "Sim" : "Não"));
                item.add(new ExternalLink("relatorioPdfInd", request.endereco + "endereco/relatorioPdf?id=" + endereco.getId()));
                item.add(new ExternalLink("relatorioExInd", request.endereco + "endereco/relatorioExcel?id=" + endereco.getId()));
                item.add(new AjaxLink<>("excluir", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modal.setInitialWidth(450).setInitialHeight(300);
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

        linkPdf.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        linkExcel.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(linkPdf, linkExcel);

        add(filtroCidade.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valorSelecionado = filtroCidade.getModelObject();
                if (valorSelecionado != null) {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("cidade=") ? "cidade=" + valorSelecionado : s)
                            .collect(Collectors.joining("&"));
                } else {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("cidade=") ? "cidade=" : s)
                            .collect(Collectors.joining("&"));
                }
                filtros = filtros.replace(" ", "%20");
                List<Endereco> e = request.obter("endereco/filtrar" + filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
            }
        }));

        add(filtroEstado.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valorSelecionado = filtroEstado.getModelObject();
                if (valorSelecionado != null) {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("estado=") ? "estado=" + valorSelecionado : s)
                            .collect(Collectors.joining("&"));
                } else {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("estado=") ? "estado=" : s)
                            .collect(Collectors.joining("&"));
                }
                filtros = filtros.replace(" ", "%20");
                List<Endereco> e = request.obter("endereco/filtrar" + filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
            }
        }));

        add(filtroMonitorador.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Monitorador valorSelecionado = filtroMonitorador.getModelObject();
                if (valorSelecionado != null) {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("monitorador=") ? "monitorador=" + valorSelecionado.getId() : s)
                            .collect(Collectors.joining("&"));
                } else {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("monitorador=") ? "monitorador=" : s)
                            .collect(Collectors.joining("&"));
                }
                filtros = filtros.replace(" ", "%20");
                List<Endereco> e = request.obter("endereco/filtrar" + filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
            }
        }));

        add(pesquisar.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String valor = pesquisar.getModelObject();
                if (valor != null) {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("text=") ? "text=" + valor : s)
                            .collect(Collectors.joining("&"));
                } else {
                    filtros = Arrays.stream(filtros.split("&"))
                            .map(s -> s.contains("text=") ? "text=" : s)
                            .collect(Collectors.joining("&"));
                }
                filtros = filtros.replace(" ", "%20");
                List<Endereco> e = request.obter("endereco/filtrar" + filtros, Endereco.class);
                Collections.sort(e);
                listView.setList(e);
                atualizarRelatorios();
                target.add(container, linkPdf, linkExcel);
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
            public void onClick(AjaxRequestTarget target) {
                modal.setInitialWidth(600).setInitialHeight(500);
                modal.setContent(new ModalEndereco(modal.getContentId(), modal, new Endereco()));
                modal.show(target);
            }
        });

        modal.setWindowClosedCallback((ModalWindow.WindowClosedCallback) target -> {
            List<Endereco> e = request.getEnderecoList();
            listView.setList(e);
            target.add(container);
        });

        container.add(new PagingNavigator("navigator", listView));
        container.add(listView);
        add(filtroEstado, filtroCidade, filtroMonitorador, pesquisar, container, modal);
    }

    private List<String> getEstados() {
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

    private List<Monitorador> getMonitoradores() {
        List<Monitorador> monitoradores = new ArrayList<>();
        for (Monitorador m : request.getMonitoradoresList()) {
            if (!m.getEnderecos().isEmpty()) {
                monitoradores.add(m);
            }
        }
        return monitoradores;
    }

    private String getNomeMonitorador(String cep){
        for (Monitorador m : request.getMonitoradoresList()) {
            for (Endereco e : m.getEnderecos()) {
                if (Objects.equals(e.getCep(), cep)) {
                    return m.getNomeOrRazao();
                }
            }
        }
        return "";
    }

    private void atualizarRelatorios() {
        String urlPdf = request.endereco + "endereco/relatorioPdf" + filtros;
        String urlExcel = request.endereco + "endereco/relatorioExcel" + filtros;
        linkPdf.setDefaultModel(Model.of(urlPdf));
        linkExcel.setDefaultModel(Model.of(urlExcel));
    }
}

