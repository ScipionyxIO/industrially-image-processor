package io.scipionyx.industrially.imagerecon.repository;

import io.scipionyx.industrially.imagerecon.model.Modeling;
import org.springframework.data.repository.CrudRepository;

public interface ModelingRepository extends CrudRepository<Modeling, Long> {

    //Modeling findOneByName(@Param("name") String name);

}
