package dev.erikmota.desafiounikamain;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class DashboardPage extends BasePage {
    public DashboardPage() {

        atualizarListas();

        add(new Label("quantMonitorador", monitoradorList.size()));
        add(new Label("quantEndereco", enderecoList.size()));

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
