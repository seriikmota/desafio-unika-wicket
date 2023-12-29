package dev.erikmota.desafiounikamain.modals;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class CadastrarMonitorador extends Panel {
    public CadastrarMonitorador(String id){
        super(id);

        Form<Void> form = new Form<Void>("formMonitorador"){
            @Override
            protected void onSubmit() {
                System.out.println("Form submitted.");
            }
        };

        add(form);
    }
    
}
