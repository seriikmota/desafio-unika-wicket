package dev.erikmota.desafiounikamain.modals;

import dev.erikmota.desafiounikamain.MonitoradorPage;
import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class ModalExcluir extends Panel {
    private static final ActionsRequest request = ActionsRequest.getInstance();
    public ModalExcluir(String id, ModalWindow modal, String path) {
        super(id);

        add(new AjaxLink<Void>("excluir") {
            @Override
            public void onClick(AjaxRequestTarget target){
                request.excluir(path);
                modal.close(target);
            }
        });
        add(new AjaxLink<Void>("close") {
            @Override
            public void onClick(AjaxRequestTarget target){
                modal.close(target);
            }
        });
    }
}
