package eu.arrowhead.application.skeleton.consumer;

import eu.arrowhead.application.skeleton.consumer.dto.PersonResponseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.InvalidParameterException;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, "ai.aitia"}) //TODO: add custom packages if any
public class ConsumerMain implements ApplicationRunner {

    //=================================================================================================
	// members

    @Autowired
	private ArrowheadService arrowheadService;

	@Autowired
	protected SSLProperties sslProperties;

	private final Logger logger = LogManager.getLogger( ConsumerMain.class );

    //=================================================================================================
	// methods

	//------------------------------------------------------------------------------------------------
    public static void main( final String[] args ) {
    	SpringApplication.run(ConsumerMain.class, args);
    }

	// Extra methods
	/**
	 * This method validates the orchestration result.
	 * @param orchestrationResult
	 * @param serviceDefinition
	 */
	private void validateOrchestrationResult(final OrchestrationResultDTO orchestrationResult, final String serviceDefinition) {
		if (!orchestrationResult.getService().getServiceDefinition().equalsIgnoreCase(serviceDefinition)) {
			throw new InvalidParameterException("Requested and orchestrated service definition do not match");
		}

		boolean hasValidInterface = false;
		for (final ServiceInterfaceResponseDTO serviceInterface : orchestrationResult.getInterfaces()) {
			if (serviceInterface.getInterfaceName().equalsIgnoreCase(getInterface())) {
				hasValidInterface = true;
				break;
			}
		}
		if (!hasValidInterface) {
			throw new InvalidParameterException("Requested and orchestrated interface do not match");
		}
	}

	/**
	 * This method returns the interface based on the SSL configuration.
	 * @return
	 */
	private String getInterface() {
		return sslProperties.isSslEnabled() ? ConsumerConstants.INTERFACE_SECURE : ConsumerConstants.INTERFACE_INSECURE;
	}

	/**
	 * Homemade method to prettify the JSON object.
	 * @param obj
	 */
	private void prettifyJson(final Object obj) {
			System.out.println(Utilities.toPrettyJson(Utilities.toJson(obj)));
	}

    //-------------------------------------------------------------------------------------------------
    @Override
	public void run(final ApplicationArguments args) throws Exception {

		logger.info("Starting orchestration request for id-card-reader service");

		try {
			// Creating the service query form with the service definition and the interface.
			logger.debug("Building ServiceQueryFormDTO with service definition: id-card-reader and interface: " + getInterface());
			final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder("id-card-reader")
					.interfaces(getInterface())
					.build();
			logger.info("Service query form constructed:");
			prettifyJson(serviceQueryForm);

			// Building the orchestration form request.
			logger.debug("Building OrchestrationFormRequestDTO");
			final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
			final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder
					.requestedService(serviceQueryForm)
					.flag(Flag.OVERRIDE_STORE, true)  // Setting OVERRIDE_STORE flag to true
					.build();

			logger.info("Orchestration request constructed:");
			prettifyJson(orchestrationFormRequest);

			// Sending the orchestration request and receiving the response.
			logger.debug("Sending orchestration request to the Arrowhead Orchestrator...");
			OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);

			logger.info("Orchestration response received:");
			prettifyJson(orchestrationResponse);

			// Processing the orchestration response.
			if (orchestrationResponse == null) {
				logger.error("Orchestration response is null. Throwing InvalidParameterException...");
				throw new InvalidParameterException("Orchestration response is empty");
			} else if (orchestrationResponse.getResponse().isEmpty()) {
				logger.error("Orchestration response is empty. Throwing InvalidParameterException...");
				throw new InvalidParameterException("Orchestration response is empty");
			} else {
				final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
				logger.debug("Validating orchestration result for service: id-card-reader");
				validateOrchestrationResult(orchestrationResult, "id-card-reader");

				logger.info("Orchestration result validated successfully.");

				// Consuming the service from the provider.
				final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());

				logger.info("Consuming the id-card-reader service from provider at " + orchestrationResult.getProvider().getAddress() + ":" + orchestrationResult.getProvider().getPort());
				final List<PersonResponseDTO> allPeople = arrowheadService.consumeServiceHTTP(
						List.class,
						HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
						orchestrationResult.getProvider().getAddress(),
						orchestrationResult.getProvider().getPort(),
						orchestrationResult.getServiceUri(),
						getInterface(),
						token,
						null,
						new String[0]
				);

				logger.info("Successfully consumed the service. Response received:");
				prettifyJson(allPeople);
			}
		} catch (InvalidParameterException e) {
			logger.error("InvalidParameterException caught: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("An unexpected error occurred during the orchestration or service consumption process: " + e.getMessage(), e);
		}
		
	/**
	 * The following is pre-implemented code from AITIA.
	 * */

/*	//SIMPLE EXAMPLE OF INITIATING AN ORCHESTRATION

	final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();

	final ServiceQueryFormDTO requestedService = new ServiceQueryFormDTO();
	requestedService.setServiceDefinitionRequirement("id-card-reader"); //Service definition should be the same as in the service registry.

	orchestrationFormBuilder.requestedService(requestedService)
							.flag(Flag.MATCHMAKING, false) //When this flag is false or not specified, then the orchestration response cloud contain more proper provider. Otherwise only one will be chosen if there is any proper.
							.flag(Flag.OVERRIDE_STORE, true) //When this flag is false or not specified, then a Store Orchestration will be proceeded. Otherwise a Dynamic Orchestration will be proceeded.
							.flag(Flag.TRIGGER_INTER_CLOUD, false); //When this flag is false or not specified, then orchestration will not look for providers in the neighbor clouds, when there is no proper provider in the local cloud. Otherwise it will.

	final OrchestrationFormRequestDTO orchestrationRequest = orchestrationFormBuilder.build();

	OrchestrationResponseDTO response = null;
	try {
		response = arrowheadService.proceedOrchestration(orchestrationRequest);
	} catch (final ArrowheadException ex) {
		//Handle the unsuccessful request as you wish!
		logger.debug("Orchestration request failed: " + ex.getMessage());
	}

	//EXAMPLE OF CONSUMING THE SERVICE FROM A CHOSEN PROVIDER

	if (response == null || response.getResponse().isEmpty()) {
		//If no proper providers found during the orchestration process, then the response list will be empty. Handle the case as you wish!
		logger.debug("Orchestration response is empty");
		return;
	} else {
		logger.debug("Orchestration response received: " + response);
	}

	final OrchestrationResultDTO result = response.getResponse().get(0); //Simplest way of choosing a provider.

	final HttpMethod httpMethod = HttpMethod.GET;//Http method should be specified in the description of the service.
	final String address = result.getProvider().getAddress();
	final int port = result.getProvider().getPort();
	final String serviceUri = result.getServiceUri();
	final String interfaceName = result.getInterfaces().get(0).getInterfaceName(); //Simplest way of choosing an interface.
	String token = null;
	if (result.getAuthorizationTokens() != null) {
		token = result.getAuthorizationTokens().get(interfaceName); //Can be null when the security type of the provider is 'CERTIFICATE' or nothing.
	}
	final Object payload = null; //Can be null if not specified in the description of the service.

	final String consumedService = arrowheadService.consumeServiceHTTP(String.class, httpMethod, address, port, serviceUri, interfaceName, token, payload, "testkey", "testvalue");*/
	}
}
