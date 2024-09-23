package eu.arrowhead.application.skeleton.provider.repository;

import eu.arrowhead.application.skeleton.provider.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
}