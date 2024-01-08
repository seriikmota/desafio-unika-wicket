package dev.erikmota.desafiounikamain.modals;


import dev.erikmota.desafiounikamain.service.ActionsRequest;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;
public class ModalImportar extends Panel {
    private final String endereco = "http://localhost:8081/monitorador/importar";
    private static final ActionsRequest request = new ActionsRequest();
    public ModalImportar(String id){
        super(id);
        FileUploadField fileUploadField = new FileUploadField("importar");

        Form<Void> form = new Form<>("form"){
            @Override
            protected void onSubmit() {
                super.onSubmit();

                FileUpload fileUpload = fileUploadField.getFileUpload();

                try{
                    File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileUpload.getClientFileName());
                    fileUpload.writeTo(file);
                    request.importar(endereco, file);
                    info("Upload completed!");
                } catch (Exception e){
                    e.printStackTrace();
                    error("Upload failed");
                }
            }
        };

        form.setMultiPart(true);
        form.setMaxSize(Bytes.kilobytes(100));
        form.add(fileUploadField);
        add(new FeedbackPanel("feedbackPanel"));
        add(form);
    }
}
