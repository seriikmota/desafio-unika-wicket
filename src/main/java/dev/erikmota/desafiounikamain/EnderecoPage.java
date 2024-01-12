package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class EnderecoPage extends BasePage {
    private static final ActionsRequest request = new ActionsRequest();
    public EnderecoPage() {
        request.atualizarListas();

        try{
            add(new ListView<>("enderecoList", request.getEnderecoList()) {
                @Override
                protected void populateItem(ListItem<Endereco> item) {
                    final Endereco endereco = item.getModelObject();
                    item.add(new Label("eId", endereco.getId()));
                    item.add(new Label("eCep", endereco.getCep()));
                    item.add(new Label("eEndereco", endereco.getEndereco()));
                    item.add(new Label("eNumero", endereco.getNumero()));
                    item.add(new Label("eBairro", endereco.getBairro()));
                    item.add(new Label("eTelefone", endereco.getTelefone()));
                    item.add(new Label("eCidade", endereco.getCidade()));
                    item.add(new Label("eEstado", endereco.getEstado()));
                    item.add(new Label("eMonitorador", endereco.getNomeOrRazao()));
                    item.add(new Label("ePrincipal", endereco.getPrincipal()));
                }
            });
        } catch (NullPointerException e){
            e.printStackTrace();
        }

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
    }
}
