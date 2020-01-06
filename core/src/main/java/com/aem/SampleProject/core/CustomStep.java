/**
 * Sample project
 */
package com.aem.SampleProject.core;

import org.slf4j.Logger;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

@Component(service=WorkflowProcess.class, property= {"process.label= My Email Custom Step"})
public class CustomStep implements WorkflowProcess {
	final protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private ResourceResolverFactory resolverFactory ;
	
	@Reference
	private MessageGatewayService messageGatewayService;
	
	private Session session;

	@Override
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		// TODO Auto-generated method stub
		try {
			logger.info("Method execution starts");
			MessageGateway<Email> messageGateway;
			Email email = new SimpleEmail();
			
			String emailToRecipient ="giles_kisife@yahoo.ca";
			String emailCCRecioient = "kiskin8086@yahoo.com";
			email.addTo(emailToRecipient);
			email.addTo(emailCCRecioient);
			email.setSubject("My AEM Custom step");
			email.setFrom("scottm@addobe.com");
			email.setMsg(" This is the email message");
			
			messageGateway = messageGatewayService.getGateway(Email.class);
			messageGateway.send((Email)email);
			
					
		}
		catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}

}
