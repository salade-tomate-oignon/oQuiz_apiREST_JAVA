package org.stoe.oquiz.config;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	/**
	 * Autorise le "Cross-origin resource sharing" avec le domaine de l'IHM
     * 
	 */
	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		
		response.getHttpHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost:4200");
		response.getHttpHeaders().putSingle("Access-Control-Allow-Credentials", "true");
		response.getHttpHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
		response.getHttpHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization");
        
		return response;
	}

}
