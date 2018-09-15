package io.scipionyx.industrially.imagerecon.repository;

import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface ObservationRepository extends CrudRepository<Observation, Long> {

    Stream<Observation> streamByModeling(Modeling modeling);

}
