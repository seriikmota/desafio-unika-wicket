package dev.erikmota.desafiounikamain.modals;

import dev.erikmota.desafiounikamain.MonitoradorPage;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;

public class ModalConfirmacao extends Panel {
    private static final ActionsRequest request = new ActionsRequest();
    final String endereco = "http://localhost:8081/monitorador";
    public ModalConfirmacao(String id, ModalWindow modal, Long mId) {
        super(id);
        add(new AjaxLink<Void>("confirm") {
            @Override
            public void onClick(AjaxRequestTarget target){
                System.out.println("confirm");
                modal.close(target);
                setResponsePage(MonitoradorPage.class);
            }
        });
        add(new AjaxLink<Void>("fechar") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.close(target);
            }
        });
    }
}
