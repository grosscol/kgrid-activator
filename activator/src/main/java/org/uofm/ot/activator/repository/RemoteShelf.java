package org.uofm.ot.activator.repository;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.uofm.ot.activator.exception.OTExecutionBadGateway;
import org.uofm.ot.activator.exception.OTExecutionStackException;
import org.uofm.ot.activator.domain.ArkId;
import org.uofm.ot.activator.domain.KnowledgeObject;


@Service
public class RemoteShelf {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public KnowledgeObject checkOutByArkId(ArkId arkId) throws OTExecutionStackException {
		RestTemplate rt = new RestTemplate();

		KnowledgeObject object = null;
		
		try {

			// This creates a client that redirects on gets for HTTP 3xx redirect responses.
			HttpClient instance = HttpClientBuilder.create()
					.setRedirectStrategy(new DefaultRedirectStrategy()).build();

			RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory(instance));

			ResponseEntity<KnowledgeObject> response = rest.getForEntity(getAbsoluteObjectUrl(arkId) + "/complete", KnowledgeObject.class);

			object = response.getBody() ; 

			object.url = getAbsoluteObjectUrl(arkId) ; 

			log.info("KnowledgeObject with Ark Id: "+ arkId + "is checked out from : "+ getAbsoluteObjectUrl(arkId) );
		} catch ( HttpClientErrorException e ) {
			if(e.getRawStatusCode() == HttpStatus.NOT_FOUND.value() ){
				throw new OTExecutionBadGateway("Object with Ark Id : "+arkId+" does not exist ");
			} else {
				throw new OTExecutionStackException(e);
			}
		}
		
		
		return object;
	}


	@Value("${library.url:}")
	String libraryAbsolutePath;

	public String getLibraryPath() {

		String path;

		ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

		if (libraryAbsolutePath.isEmpty()) {
			path = uriBuilder.replacePath("").toUriString();
		} else {
			path = libraryAbsolutePath;
		}

		return path;
	}


	public String getAbsoluteObjectUrl(ArkId arkId) {
		return getLibraryPath()+"/knowledgeObject/" +arkId.getArkId();
	}
}
