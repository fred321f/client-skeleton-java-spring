package eu.arrowhead.application.skeleton.provider;

import eu.arrowhead.application.skeleton.provider.entity.PersonEntity;
import eu.arrowhead.application.skeleton.provider.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, "ai.aitia"}) //TODO: add custom packages if any
public class ProviderMain {

    //=================================================================================================
    // members
    //-------------------------------------------------------------------------------------------------

    @Autowired
    PersonRepository personRepository;

    //=================================================================================================
    // methods
    //-------------------------------------------------------------------------------------------------

    public static void main(final String[] args) {
        SpringApplication.run(ProviderMain.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {


            Iterable<PersonEntity> persons = personRepository.findAll();

            System.out.println("Provider is running...");
            System.out.println("Checking if database is seeded...");

            // Check if the database is seeded. If empty, seed.
            if (personRepository.findAll().isEmpty()) {
                System.out.println("Database is empty. Attempting to seed...");
                try {
                    System.out.println("Attempting to seed database...");
                    PersonEntity person1 = new PersonEntity("John Doe", 123456, 654321, 987654);
                    personRepository.save(person1);
                    System.out.println("Database seeded successfully.");
                } catch (Exception e) {
                    System.out.println("Error while seeding database: " + e.getMessage());
                }
            } else {
                System.out.println("Database is already seeded with following data:");

                for (PersonEntity person : persons) {
                    System.out.println(
                            "\nEntry ID: " + person.getId() +
                            "\nName: " + person.getName() +
                            "\nFingerprint hash: " + person.getFingerprintHash() +
                            "\nID card hash: " + person.getIdCardHash() +
                            "\nFacial recognition hash: " + person.getFacialRecogHash()
                    );
                    System.out.println(
                            "\n--------------------------------------------"
                    );
                }
            }
        };
    }
}
