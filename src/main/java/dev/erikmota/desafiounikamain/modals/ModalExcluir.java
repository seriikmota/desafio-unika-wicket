package dev.erikmota.desafiounikamain.modals;

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
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);

        add(feedback);

        add(new AjaxLink<Void>("excluir") {
            @Override
            public void onClick(AjaxRequestTarget target){
                String feedbackString = request.excluir(path);
                feedback.info(feedbackString.substring(5));
                target.add(feedback);

                if (feedbackString.startsWith("200")) {
                    target.appendJavaScript("setTimeout(function(){ Wicket.Window.get().close(); }, 900);");
                }
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
