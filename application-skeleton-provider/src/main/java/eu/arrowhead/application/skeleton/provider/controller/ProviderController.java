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
	//=================================================================================================

	//TODO: add your variables here
	@Autowired
	private ArrowheadService arrowheadService;

	//=================================================================================================
	// methods
	//=================================================================================================

	/*
	 -------------------------------------------------------------------
	|  CREATE PROVIDER SYSTEM (with services)                           |
	|-------------------------------------------------------------------|
	|  systemName: Matches the name in the application.properties file  |
	|  address: Matches the address in the application.properties file  |
	|  port: Whatever port you want to register the service on          |
	|  authenticationInfo: Public key for authorization.                |
	|___________________________________________________________________|
	*/
	private void registerService(String serviceDefinition, String serviceUri) {
        try {
			System.out.println("Registering service: " + serviceDefinition);
            ServiceRegistryRequestDTO serviceRegistryRequest = new ServiceRegistryRequestDTO();
            serviceRegistryRequest.setServiceDefinition(serviceDefinition);

            serviceRegistryRequest.setProviderSystem(new SystemRequestDTO(
                    "sysop",
                    "localhost",
                    8888,
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqWdqKIUsgO1ow3VWCMUGa3hd/kmC8iDvmE3JuVMdgrNt4AOTye5KNYva3vQ0iQ91gguQ+OfjOSkUETzx/jwfMqQzPBzircqZE4+kB1VS+TvOkZCxhcfP5rW/cGt5KpyhCmjwpGZm+0tmYdZySF0VfAuC2SF8K9NPSOtxM8fivOwMyW5UDrRUpJRxwQV70cxWdS2qIS84hI+hUSvHxo+FHMYeVAjLSz4N0iyPjrPQLhCFDl/4oMtsZ3YZ60iwXT8EKtUFoulHasaI6Mmz95h2nM0TuHsCx9x2/9wUjn504kVzjRkK7Vw18LR2YBrGHSLRWsTzEcMUlRIq00Is2gD8+QIDAQAB",
                    null
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
			System.out.println("registerService() executed.");
		}

    }

	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}

	@PostConstruct
	public void init() {

		/** FINGERPRINT SENSOR **/
		String serviceDefinition1 = "fingerprint-sensor";

		// Register Fingerprint Sensor service to Service Registry
		ServiceRegistryRequestDTO serviceRegistryRequest = new ServiceRegistryRequestDTO();
		serviceRegistryRequest.setServiceDefinition(serviceDefinition1);

		serviceRegistryRequest.setProviderSystem(new SystemRequestDTO(
				"lenovo-provider",
				"localhost",
				8888,
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqWdqKIUsgO1ow3VWCMUGa3hd/kmC8iDvmE3JuVMdgrNt4AOTye5KNYva3vQ0iQ91gguQ+OfjOSkUETzx/jwfMqQzPBzircqZE4+kB1VS+TvOkZCxhcfP5rW/cGt5KpyhCmjwpGZm+0tmYdZySF0VfAuC2SF8K9NPSOtxM8fivOwMyW5UDrRUpJRxwQV70cxWdS2qIS84hI+hUSvHxo+FHMYeVAjLSz4N0iyPjrPQLhCFDl/4oMtsZ3YZ60iwXT8EKtUFoulHasaI6Mmz95h2nM0TuHsCx9x2/9wUjn504kVzjRkK7Vw18LR2YBrGHSLRWsTzEcMUlRIq00Is2gD8+QIDAQAB",
				null
        ));

		serviceRegistryRequest.setServiceUri("/fingerprint-sensor");
		serviceRegistryRequest.setSecure("CERTIFICATE");
		serviceRegistryRequest.setInterfaces(Collections.singletonList("HTTP-SECURE-JSON"));

		arrowheadService.forceRegisterServiceToServiceRegistry(serviceRegistryRequest);

		/** ID CARD READER **/
		String serviceDefinition2 = "id-card-reader";

		// Register ID Card Reader service to Service Registry
		ServiceRegistryRequestDTO serviceRegistryRequest2 = new ServiceRegistryRequestDTO();
		serviceRegistryRequest2.setServiceDefinition(serviceDefinition2);

		serviceRegistryRequest2.setProviderSystem(new SystemRequestDTO(
				"lenovo-provider",
				"localhost",
				8888,
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoAROuadv5+DIIzwy8tsHfknH4PfRHteZu5K4CzhsdI05HWPz2hYULVP9MP2C7tqQ1cC7lDYY/zC0PWKS95mu/2eaQAmM2Yc+n4uL7320u0Qwb5W1rUy5hR57huQgoAYlbvAj1G51eiQciIMUV2kkhZnx90VLdr7dCPEBkmKNh5ViaQVLkjOKHt+WED4qTUYmvruJuNTzadC6mirlDD41PXTsDTqQ29XYIFtwJlDaTDFzOvwwbeHv5Ar8zOF7gl4MTWhuCe9IBiNaqXgi92ZhtYMGUfUTIXvHcsqeqtj+HyQvOjr7mA9zb5XCTXR7/NHRnAiV9VXlH10FXS0yO64rJQIDAQAB",
				null
		));

		serviceRegistryRequest2.setServiceUri("/id-card-reader");
		serviceRegistryRequest2.setSecure("CERTIFICATE");
		serviceRegistryRequest2.setInterfaces(Collections.singletonList("HTTP-SECURE-JSON"));

		arrowheadService.forceRegisterServiceToServiceRegistry(serviceRegistryRequest2);

		/** Facial Recog Cam **/
		String serviceDefinition3 = "facial-fecognition-camera";

		// Register Facial Recognition Camera service to Service Registry
		ServiceRegistryRequestDTO serviceRegistryRequest3 = new ServiceRegistryRequestDTO();
		serviceRegistryRequest3.setServiceDefinition(serviceDefinition3);

		serviceRegistryRequest3.setProviderSystem(new SystemRequestDTO(
				"lenovo-provider",
				"localhost",
				8888,
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqWdqKIUsgO1ow3VWCMUGa3hd/kmC8iDvmE3JuVMdgrNt4AOTye5KNYva3vQ0iQ91gguQ+OfjOSkUETzx/jwfMqQzPBzircqZE4+kB1VS+TvOkZCxhcfP5rW/cGt5KpyhCmjwpGZm+0tmYdZySF0VfAuC2SF8K9NPSOtxM8fivOwMyW5UDrRUpJRxwQV70cxWdS2qIS84hI+hUSvHxo+FHMYeVAjLSz4N0iyPjrPQLhCFDl/4oMtsZ3YZ60iwXT8EKtUFoulHasaI6Mmz95h2nM0TuHsCx9x2/9wUjn504kVzjRkK7Vw18LR2YBrGHSLRWsTzEcMUlRIq00Is2gD8+QIDAQAB",
				null
		));

		serviceRegistryRequest3.setServiceUri("/facial-recog-cam");
		serviceRegistryRequest3.setSecure("CERTIFICATE");
		serviceRegistryRequest3.setInterfaces(Collections.singletonList("HTTP-SECURE-JSON"));

		arrowheadService.forceRegisterServiceToServiceRegistry(serviceRegistryRequest3);

	}

	//-------------------------------------------------------------------------------------------------
	//TODO: implement here your provider related REST end points

	@GetMapping
	public String provideService() {
		return "Hello from Lenovo Laptop!";
	}
}
