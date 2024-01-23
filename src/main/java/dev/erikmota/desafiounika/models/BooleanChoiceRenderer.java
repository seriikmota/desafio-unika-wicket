package dev.erikmota.desafiounika.models;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class BooleanChoiceRenderer extends ChoiceRenderer<Boolean>{
    @Override
    public Object getDisplayValue(Boolean object) {
        return object ? "Sim" : "Não";
    }
    @Override
    public String getIdValue(Boolean object, int index) {
        return String.valueOf(index);
    }
}
