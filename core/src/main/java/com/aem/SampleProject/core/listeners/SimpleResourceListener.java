/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.SampleProject.core.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.api.resource.LoginException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.component.ComponentContext;
import javax.jcr.observation.EventIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aem.SampleProject.core.models.Model;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
    
import javax.jcr.Repository; 
import javax.jcr.SimpleCredentials; 
import javax.jcr.Node; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap; 
import java.util.Map; 
     
import org.apache.jackrabbit.commons.JcrUtils;
    
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
    
//import org.apache.felix.scr.annotations.Component;
//import org.apache.felix.scr.annotations.Service;
import javax.jcr.RepositoryException;
//import org.apache.felix.scr.annotations.Reference;
import org.apache.jackrabbit.commons.JcrUtils;
    
import javax.jcr.Session;
import javax.jcr.Node; 
   
   
//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 
    

@Component(immediate = true, service = EventListener.class)

public class SimpleResourceListener implements EventListener {
	final Map<String, Object> param = new HashMap<String, Object>();
	private List<Model> custList = new ArrayList<Model>();

	Logger log = LoggerFactory.getLogger(this.getClass());
	private Session adminSession;

	@Reference
	org.apache.sling.jcr.api.SlingRepository repository;
	
	@Reference
	ResourceResolverFactory resolverFactory;

	@Activate
	public String activate(ComponentContext context) throws Exception {
		log.info("Trying to activate ExampleObservation");
		param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
		ResourceResolver resolver = null;
		try {
			
			 resolver = resolverFactory.getServiceResourceResolver(param);
					 
			 adminSession = resolver.adaptTo(Session.class);
			//adminSession = repository.loginService("datawrite", null);
			log.info(" Passed first level");
			adminSession.getWorkspace().getObservationManager().addEventListener(this, // handler
					Event.PROPERTY_ADDED | Event.NODE_ADDED, // binary combination of event types
					"/apps/sampleproject", // path
					true, // is Deep?
					null, // uuids filter
					null, // nodetypes filter
					false);
			log.info("ExampleObservation listener has been activated");
			//Obtain the query manager for the session ...
		    javax.jcr.query.QueryManager queryManager = adminSession.getWorkspace().getQueryManager();
		        
			
			String  sqlStatement = "SELECT * FROM [nt:unstructured] ";
			javax.jcr.query.Query query = queryManager.createQuery(sqlStatement,"JCR-SQL2");
			
			   
			//Execute the query and get the results ...
			javax.jcr.query.QueryResult result = query.execute();
			   
			//Iterate over the nodes in the results ...
			javax.jcr.NodeIterator nodeIter = result.getNodes();
			   
			while ( nodeIter.hasNext() ) {
			   
			  //For each node-- create a customer instance
			
			           
			 javax.jcr.Node node = nodeIter.nextNode();
			 Model cust = new Model();
			             
			 //Set all Customer object fields
			 cust.setTitle(node.getProperty("jcr:title").getString());
			 cust.setPath(node.getProperty("jcr:primaryType").getString());
			 //cust.setCustAddress(node.getProperty("address").getString());
			 //cust.setCustDescription(node.getProperty("desc").getString());
			             
			  //Push Customer to the list
			  //custList.add(cust);
			  }
			           
			// Log out
			 adminSession.logout();    
			return convertToString(toXml(custList));      

		} catch (RepositoryException e) {
			log.error("unable to register session", e);
			throw new Exception(e);
		}
	}

	@Deactivate
	public void deactivate() {
		if (adminSession != null) {
			adminSession.logout();
		}
	}

	public void onEvent(EventIterator eventIterator) {
		try {
			while (eventIterator.hasNext()) {
				log.info("something has been added : {}", eventIterator.nextEvent().getPath());
			}
		} catch (RepositoryException e) {
			log.error("Error while treating events", e);
		}
	}
	
	
	private String convertToString(Document xml)
	{
	try {
	   Transformer transformer = TransformerFactory.newInstance().newTransformer();
	  StreamResult result = new StreamResult(new StringWriter());
	  DOMSource source = new DOMSource(xml);
	  transformer.transform(source, result);
	  return result.getWriter().toString();
	} catch(Exception ex) {
	      ex.printStackTrace();
	}
	  return null;
	 }
	
	

    
	//Convert Customer data retrieved from the AEM JCR
	//into an XML schema to pass back to client
	private Document toXml(List<Model> custList) {
	try
	{
	    DocumentBuilderFactory factory =     DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.newDocument();
	                    
	    //Start building the XML to pass back to the AEM client
	    Element root = doc.createElement( "Customers" );
	    doc.appendChild( root );
	                 
	    //Get the elements from the collection
	    int custCount = custList.size();
	       
	    //Iterate through the collection to build up the DOM           
	     for ( int index=0; index < custCount; index++) {
	    
	         //Get the Customer object from the collection
	         Model myModel = (Model)custList.get(index);
	                         
	         Element Customer = doc.createElement( "Customer" );
	         root.appendChild( Customer );
	                          
	         //Add rest of data as child elements to customer
	         //Set First Name
	         Element first = doc.createElement( "Title" );
	         first.appendChild( doc.createTextNode(myModel.getTitle() ) );
	         Customer.appendChild( first );
	                                                             
	         //Set Last Name
	         Element last = doc.createElement( "Path" );
	         last.appendChild( doc.createTextNode(myModel.getPath()) );
	         Customer.appendChild( last );
	                       
	      }
	               
	return doc;
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    }
	return null;
	}
}