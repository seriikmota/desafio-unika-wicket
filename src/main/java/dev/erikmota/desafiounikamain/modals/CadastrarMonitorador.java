package dev.erikmota.desafiounikamain.modals;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CadastrarMonitorador extends Panel {

    private TextField cpfField;
    private TextField cnpjField;
    private TextField nomeField;

    public CadastrarMonitorador(String id){
        super(id);

        Monitorador monitorador = new Monitorador();
        Form<Monitorador> form = new Form<>("formMonitorador", new CompoundPropertyModel<>(monitorador)){
            @Override
            protected void onSubmit() {
                System.out.println(monitorador);

            }
        };
        TextField<String> cpfField = new TextField<>("cpf");
        TextField<String> cnpjField = new TextField<>("cnpj");
        TextField<String> nomeField = new TextField<>("nome");
        DropDownChoice<TipoPessoa> tipoPessoaDropDown = new DropDownChoice<>("tipoPessoa", Arrays.asList(TipoPessoa.values()));
        List<String> ativoList = Arrays.asList("Sim", "Nao");
        RadioChoice<String> ativo = new RadioChoice<>("ativo", ativoList);
        form.add(ativo);
        form.add(tipoPessoaDropDown);
        form.add(cpfField);
        form.add(cnpjField);
        form.add(nomeField);

        add(form);
    }
}
