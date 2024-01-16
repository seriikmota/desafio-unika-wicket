package dev.erikmota.desafiounikamain.models;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

public class MonitoradorDropDownChoice extends DropDownChoice<Monitorador>{
    public MonitoradorDropDownChoice(String id, IModel<Monitorador> model, List<Monitorador> monitoradores) {
        super(id, model, monitoradores, new MonitoradorChoiceRenderer());
    }
    private static class MonitoradorChoiceRenderer implements IChoiceRenderer<Monitorador> {
        @Override
        public Object getDisplayValue(Monitorador monitorador) {
            return monitorador.getNomeOrRazao();
        }
        @Override
        public String getIdValue(Monitorador monitorador, int index) {
            return String.valueOf(monitorador.getId());
        }
    }
}
