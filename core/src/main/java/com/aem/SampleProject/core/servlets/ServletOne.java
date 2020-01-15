package com.aem.SampleProject.core.servlets;

import org.osgi.framework.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Repository;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
 
/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */

/**
@Component(service = { ServletOne.class })
@SlingServlet(	paths="/bin/custom/")
@SlingServletResourceTypes(
    resourceTypes="/bin/my/test/", 
    methods= "GET",
    extensions="html",
    selectors="*")
    **/

@Component(service=Servlet.class,
property= {Constants.SERVICE_DESCRIPTION +" =Simple sling servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes="+"weretail/components/structure/page",
		"sling.servlet.selectors="+"servlet1"
	})
public class ServletOne extends SlingSafeMethodsServlet{
	
	private static final long serialVersionUID = 1L;
	@Reference
    private Repository repository;
    
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String keys[] = repository.getDescriptorKeys();
        JSONObject jsonobject = new JSONObject();    
        for(int i=0;i<keys.length;i++){
            try {
                jsonobject.put(keys[i], repository.getDescriptor(keys[i]));
                
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        resp.getWriter().println(jsonobject.toString());
         
    }
}