package dev.erikmota.desafiounika.modals;

import dev.erikmota.desafiounika.service.ActionsRequest;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;
public class ModalImportar extends Panel {
    private static final ActionsRequest request = ActionsRequest.getInstance();
    public ModalImportar(String id, ModalWindow modal, String path){
        super(id);
        FileUploadField fileUploadField = new FileUploadField("file");
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        Form<Void> form = new Form<>("form");

        form.add(new AjaxButton("importar") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload fileUpload = fileUploadField.getFileUpload();
                try {
                    File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileUpload.getClientFileName());
                    fileUpload.writeTo(file);
                    String feedbackString = request.importar(path, file);
                    feedback.info(feedbackString.substring(5));
                    target.add(feedback);

                    if (feedbackString.startsWith("200"))
                        target.appendJavaScript("setTimeout(function(){ Wicket.Window.get().close(); }, 900);");
                } catch (Exception e) {
                    feedback.info("Erro: Insira o arquivo para importar!");
                    target.add(feedback);
                }
            }
        });
        form.setMultiPart(true);
        form.setMaxSize(Bytes.kilobytes(100));
        form.add(fileUploadField, feedback);

        add(new AjaxLink<Void>("close") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.close(target);
            }
        });
        add(new ExternalLink("download", request.endereco + path + "/modelo"));
        add(form);
    }
}
