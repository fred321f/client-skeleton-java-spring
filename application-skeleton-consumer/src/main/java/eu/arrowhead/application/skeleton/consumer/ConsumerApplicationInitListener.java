package eu.arrowhead.application.skeleton.consumer;

import eu.arrowhead.common.dto.shared.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.config.ApplicationInitListener;
import eu.arrowhead.common.core.CoreSystem;

import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Collections;

@Component
public class ConsumerApplicationInitListener extends ApplicationInitListener {

    //=================================================================================================
    // members
    //-------------------------------------------------------------------------------------------------

    @Autowired
    private ArrowheadService arrowheadService;

    private final Logger logger = LogManager.getLogger(ConsumerApplicationInitListener.class);

    //=================================================================================================
    // methods
    //-------------------------------------------------------------------------------------------------
    @Override
    protected void customInit(final ContextRefreshedEvent event) {
        // Check the availability of necessary core systems
        try {
            checkCoreSystemReachability(CoreSystem.SERVICEREGISTRY);
        } catch (Exception e) {
            logger.error("Service Registry not reachable: " + e.getMessage(), e);
            return;
        }

        try {
            checkCoreSystemReachability(CoreSystem.ORCHESTRATOR);
        } catch (Exception e) {
            logger.error("Orchestrator not reachable: " + e.getMessage(), e);
            return;
        }

        /*
        // Register the consumer system
        final SystemRequestDTO consumerSystem = new SystemRequestDTO();
        consumerSystem.setSystemName("consumerskeleton");
        consumerSystem.setAddress("localhost");
        consumerSystem.setPort(8888);
        consumerSystem.setAuthenticationInfo(null); // Optional: Set if required

        ServiceRegistryRequestDTO serviceRegistryRequest = new ServiceRegistryRequestDTO();
        serviceRegistryRequest.setProviderSystem(consumerSystem);
        serviceRegistryRequest.setServiceDefinition("id-card-reader"); // Adjust service definition as needed
        serviceRegistryRequest.setInterfaces(Collections.singletonList("HTTP-SECURE-JSON")); // Adjust as per your setup

        try {
            ServiceRegistryResponseDTO registrationResponse = arrowheadService.registerServiceToServiceRegistry(serviceRegistryRequest);
            logger.info("Consumer system registered successfully with ID: " + registrationResponse.getId());
        } catch (Exception e) {
            logger.error("Failed to register consumer system: " + e.getMessage(), e);
            return; // Exit if registration fails
        }

        // Prepare and send orchestration request
        OrchestrationFormRequestDTO orchestrationRequest = arrowheadService.getOrchestrationFormBuilder()
                .flag(OrchestrationFlags.Flag.MATCHMAKING, true)
                .flag(OrchestrationFlags.Flag.OVERRIDE_STORE, true)
                // TODO: When OVERRIDE_STORE is set to true, it seems to return an empty response due requested service being null. Why is this?
                // Also, when set to false, a service is registered as provider and not consumer. Still returns errors due to "Orchestration response is empty, no providers found.".
                .flag(OrchestrationFlags.Flag.TRIGGER_INTER_CLOUD, false)
                .build();

        OrchestrationResponseDTO orchestrationResponse;
        try {
            orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationRequest);
        } catch (Exception e) {
            logger.error("Orchestration failed: " + e.getMessage(), e);
            return;
        }

        if (orchestrationResponse == null || orchestrationResponse.getResponse().isEmpty()) {
            logger.error("Orchestration response is empty, no providers found.");
            return;
        }

        // Consume the service from the first provider in the response
        OrchestrationResultDTO result = orchestrationResponse.getResponse().get(0);
        String serviceUri = result.getServiceUri();
        HttpMethod httpMethod = HttpMethod.GET;
        String address = result.getProvider().getAddress();
        int port = result.getProvider().getPort();
        String interfaceName = result.getInterfaces().get(0).getInterfaceName();
        String token = result.getAuthorizationTokens() != null ? result.getAuthorizationTokens().get(interfaceName) : null;

        try {
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http") // Use "https" if using a secure connection
                    .host(address)
                    .port(port)
                    .path(serviceUri)
                    .build();

            String response = arrowheadService.consumeServiceHTTP(
                    String.class, httpMethod, uriComponents, token, null);
            logger.info("Service response: " + response);
        } catch (Exception e) {
            logger.error("Error occurred while consuming service: " + e.getMessage(), e);
        }*/

        /*// TODO: It seems to be registered as a provider, not a consumer
        // Checking the availability of necessary core systems
        checkCoreSystemReachability(CoreSystem.SERVICEREGISTRY);
        checkCoreSystemReachability(CoreSystem.ORCHESTRATOR);

        // Registering the consumer system
        final SystemRequestDTO consumerSystem = new SystemRequestDTO();
        consumerSystem.setSystemName("sysop");
        consumerSystem.setAddress("127.0.0.1");
        consumerSystem.setPort(8888);
        consumerSystem.setAuthenticationInfo("lenovo-provider" *//*arrowheadService.getMyPublicKey() != null ?
                Base64.getEncoder().encodeToString(arrowheadService.getMyPublicKey().getEncoded()) : null*//*);

        // Adding the required interface
        final String interfaceName = "HTTP-SECURE-JSON"; // Replace this with the actual interface your service supports

        try {
            ServiceRegistryRequestDTO request = new ServiceRegistryRequestDTO(
                    "example-service",
                    consumerSystem,
                    "/example-uri",
                    null,
                    "NOT_SECURE",
                    Collections.singletonList(interfaceName) // List of supported interfaces
            );

            ServiceRegistryResponseDTO response = arrowheadService.registerServiceToServiceRegistry(request);
            logger.info("Consumer system registered successfully: " + consumerSystem.getSystemName());
        } catch (Exception e) {
            logger.error("Consumer system registration failed", e);
            return;
        }

        // Proceed with orchestration
        logger.info("Orchestration starting...");
        OrchestrationFormRequestDTO.Builder orchestrationRequestBuilder = arrowheadService.getOrchestrationFormBuilder();
        OrchestrationFormRequestDTO orchestrationRequest = orchestrationRequestBuilder.build();

        try {
            OrchestrationResponseDTO response = arrowheadService.proceedOrchestration(orchestrationRequest);
            logger.info("Orchestration request sent successfully.");
        } catch (Exception e) {
            logger.error("Orchestration failed", e);
        }*/

    }

    //-------------------------------------------------------------------------------------------------

    @Override
    public void customDestroy() {
        //TODO: implement here any custom behavior on application shutdown

        // Output a log message
        logger.info("Consumer application stopped");
    }
}
