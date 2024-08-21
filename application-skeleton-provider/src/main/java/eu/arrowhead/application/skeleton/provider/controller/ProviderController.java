package eu.arrowhead.application.skeleton.provider.controller;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.arrowhead.common.CommonConstants;

import javax.annotation.PostConstruct;
import java.util.Collections;

@RestController
public class ProviderController {
	
	//=================================================================================================
	// members

	//TODO: add your variables here
	@Autowired
	private ArrowheadService arrowheadService;


	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}

	@PostConstruct
	public void init() {
		ServiceRegistryRequestDTO serviceRegistryRequest = new ServiceRegistryRequestDTO();
		serviceRegistryRequest.setServiceDefinition("lenovo_service");
		serviceRegistryRequest.setProviderSystem(new SystemRequestDTO(
				"audiocura_service_provider",
				"localhost",
				8443,
				"audiocura_service_provider",
				null
        ));

		serviceRegistryRequest.setServiceUri("/simple-service");
		serviceRegistryRequest.setSecure(null);
		serviceRegistryRequest.setInterfaces(Collections.singletonList("HTTP-SECURE-JSON"));

		arrowheadService.forceRegisterServiceToServiceRegistry(serviceRegistryRequest);
	}

	@GetMapping
	public String provideService() {
		return "Hello from Lenovo Laptop!";
	}

	//-------------------------------------------------------------------------------------------------
	//TODO: implement here your provider related REST end points
}
