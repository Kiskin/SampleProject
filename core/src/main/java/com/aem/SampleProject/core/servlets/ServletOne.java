package com.aem.SampleProject.core.servlets;

import org.osgi.framework.Constants;
import org.apache.jackrabbit.commons.JcrUtils;
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

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
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
    private Repository repository1;
    
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        
        Repository repository = null;
        
		try {
			repository = JcrUtils.getRepository("http://localhost:4502/crx/de/index.jsp");
		} catch (RepositoryException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        String keys[] = repository1.getDescriptorKeys();
        Node root = null;
        Node giles = null;
        try {
			Session session =repository1.login(new SimpleCredentials("admin","admin".toCharArray()));
			root = session.getRootNode();
			giles = root.addNode("giles");
			giles.setProperty("giles_property", "kishiy ke lanki");
			giles.setProperty("giles_double", 00.00);
			giles.setProperty("name", "Node name");
			session.save();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        JSONObject jsonobject = new JSONObject();    
        for(int i=0;i<keys.length;i++){
            try {
                jsonobject.put(keys[i], repository1.getDescriptor(keys[i]));
                
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String path  =null;
        String gilesPath = null;
        String gp = null;
        String gp1 = null;
        String gp2 = null;
		try {
			path = root.getPath();
			gilesPath = giles.getPath();
			 gp = giles.getIdentifier();
			 gp1 = giles.getUUID();
			 gp2 = giles.getName();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        resp.getWriter().println(jsonobject.toString());
        resp.getWriter().println("Node Giles is rooted at: "+ path);
        resp.getWriter().println("Giles node path is: "+gilesPath);
        resp.getWriter().println("Giles node identifer is: "+gp);
        resp.getWriter().println("Giles node UUID is: "+gp1);
        resp.getWriter().println("Giles node name is: "+gp2);
        resp.getWriter().println(req.getRequestURI());
        resp.getWriter().println(req.getPathInfo());
        resp.getWriter().println("Resquest path info is: " + req.getRequestPathInfo());
         
    }
}