package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.modals.ModalMonitorador;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class MonitoradorPage extends BasePage {
    private static final ActionsRequest request = new ActionsRequest();
    public MonitoradorPage(){
        ModalWindow modal = new ModalWindow("modal").setInitialHeight(500).setCssClassName("w_silver").setResizable(false);
        add(modal);

        request.atualizarListas();
        try {
            add(new ListView<>("monitoradorList", request.getMonitoradoresList()) {
                @Override
                protected void populateItem(ListItem<Monitorador> item) {
                    final Monitorador monitorador = item.getModelObject();
                    item.add(new Label("mId", monitorador.getId()));
                    item.add(new Label("mTipoPessoa", monitorador.getTipoPessoa()));
                    item.add(new Label("mCnpj", monitorador.getCnpj()));
                    item.add(new Label("mRazaoSocial", monitorador.getRazaoSocial()));
                    item.add(new Label("mCpf", monitorador.getCpf()));
                    item.add(new Label("mNome", monitorador.getNome()));
                    item.add(new Label("mRg", monitorador.getRg()));
                    item.add(new Label("mInscricaoEstadual", monitorador.getInscricaoEstadual()));
                    item.add(new Label("mEmail", monitorador.getEmail()));
                    item.add(new Label("mData", monitorador.getData()));
                    item.add(new Label("mQuantidadeEndereco", monitorador.getEnderecos().size()));
                    item.add(new Label("mAtivo", monitorador.getAtivo().equals(true) ? "Sim" : "Não"));
                    item.add(new AjaxLink<>("excluir", item.getModel()) {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            request.excluir("http://localhost:8081/monitorador/" + monitorador.getId());
                            setResponsePage(MonitoradorPage.class);
                        }
                    });item.add(new AjaxLink<>("editar", item.getModel()) {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            modal.setContent(new ModalMonitorador(modal.getContentId(), monitorador));
                            modal.show(target);
                        }
                    });
                }
            });
        } catch (NullPointerException e){
            System.out.println("NullPointerException");
        }

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

        add(new AjaxLink<Void>("cadastrar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.setContent(new ModalMonitorador(modal.getContentId(), new Monitorador()));
                modal.show(target);
            }
        });
    }
}