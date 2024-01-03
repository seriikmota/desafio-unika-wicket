package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class DashboardPage extends BasePage {
    private static final ActionsRequest request = new ActionsRequest();
    public DashboardPage() {

        request.atualizarListas();

        add(new Label("quantMonitorador", request.getMonitoradoresList().size()));
        add(new Label("quantEndereco", request.getEnderecoList().size()));

        add(new Link<Void>("hrefMonitorador") {
            @Override
            public void onClick() {
                setResponsePage(MonitoradorPage.class);
            }
        });

        /*add(new Link<Void>("hrefEndereco") {
            @Override
            public void onClick() {
                setResponsePage(EnderecoPage.class);
            }
        });*/
    }
}
