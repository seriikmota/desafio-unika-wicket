package dev.erikmota.desafiounikamain.models;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.*;

public class Mask extends Behavior {

    private String js;

    public Mask(String js) {
        this.js = js;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response){
        super.renderHead (component,response);

        response.render(JavaScriptReferenceHeaderItem.forUrl("https://code.jquery.com/jquery-3.6.4.min.js"));
        response.render(JavaScriptReferenceHeaderItem.forUrl("https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"));

        response.render(OnDomReadyHeaderItem.forScript("jQuery(document).ready(function(){" +
                "jQuery('#" + component.getMarkupId() + "').mask('" + js + "');" +
                "});"));

        response.render (JavaScriptHeaderItem.forUrl ("mask.js"));
    }
    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
    }

}
