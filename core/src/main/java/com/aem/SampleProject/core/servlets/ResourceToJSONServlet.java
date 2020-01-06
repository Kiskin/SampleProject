package com.aem.SampleProject.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;


@Component(service=Servlet.class,
	property= {Constants.SERVICE_DESCRIPTION +" =Simple sling servlet","sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.resourceTypes=" +"trainingproject/components/structure/page",
		"sling.servlet.paths="+ "/bin/rest"})

public class ResourceToJSONServlet extends SlingSafeMethodsServlet{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7348698713337824041L;
	//Logger logger = Logger.g     (ResourceToJSONServlet.class);
	
	

	
	@Reference
	private Repository repository;
	
	@Reference
	private QueryBuilder queryBuilder;
	
	
	@Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
		final ResourceResolver resolver = request.getResourceResolver();
		final Map<String, String> map = new HashMap<String, String>();
		
		//map.put("path", "/var/commerce/products/we-retail");
		map.put("path", "/content/dam/");
		map.put("type", "dam:Assets");
		map.put("fullText", "@jcr:title");
		try {
			@SuppressWarnings("deprecation")
			JSONObject json = new JSONObject();
			@SuppressWarnings("deprecation")
			JSONObject jsonObj ;
			
		com.day.cq.search.Query query = queryBuilder.createQuery(PredicateGroup.create(map), request.adaptTo(Session.class));
			
	final SearchResult result = query.getResult();
	//String size = String.valueOf(result.getHits().size());
			Iterator<Node> nodeIt = result.getNodes();
			//String  nodeTitles = new String();
			while(nodeIt.hasNext()) {
				
				jsonObj = new JSONObject();
				Node node = nodeIt.next();
				PropertyIterator pIt = node.getProperties();
				while(pIt.hasNext()) {
					Property p = pIt.nextProperty();
					if(p.getName().equalsIgnoreCase("jcr:title")) {
						jsonObj.append("Title", p.getName());
						
					}
					
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			
		}
	}
	
	
	
}
