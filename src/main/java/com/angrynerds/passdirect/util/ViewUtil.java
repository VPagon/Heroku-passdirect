package com.angrynerds.passdirect.util;

import org.apache.velocity.app.*;
import spark.*;
import spark.template.velocity.*;
import java.util.*;

public class ViewUtil {

    public static String render(Request request, Map<String, Object> model, String templatePath) {
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        configuredEngine.setProperty("input.encoding", "UTF-8");
        return new VelocityTemplateEngine(configuredEngine);
    }
}
