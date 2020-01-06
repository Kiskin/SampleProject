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

import java.util.HashMap;
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

@Component(immediate = true, service = EventListener.class)

public class SimpleResourceListener implements EventListener {

	Logger log = LoggerFactory.getLogger(this.getClass());
	private Session adminSession;

	@Reference
	org.apache.sling.jcr.api.SlingRepository repository;

	@Activate
	public void activate(ComponentContext context) throws Exception {
		log.info("Trying to activate ExampleObservation");
		try {
			
			adminSession = repository.loginService("datawrite", null);
			log.info(" Passed first kevel");
			adminSession.getWorkspace().getObservationManager().addEventListener(this, // handler
					Event.PROPERTY_ADDED | Event.NODE_ADDED, // binary combination of event types
					"/apps/sampleproject", // path
					true, // is Deep?
					null, // uuids filter
					null, // nodetypes filter
					false);
			log.info("ExampleObservation listener has been activated");

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
}