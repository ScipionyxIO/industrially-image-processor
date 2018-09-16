package io.scipionyx.industrially.imagerecon.service;

import io.scipionyx.industrially.imagerecon.Application;
import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import io.scipionyx.industrially.imagerecon.repository.ModelingRepository;
import io.scipionyx.industrially.imagerecon.service.observation.ObservationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@Rollback
@Transactional
public class ObservationServiceTest {

    @Autowired
    private ModelingRepository repository;

    @Autowired
    private ObservationService service;

    @Test
    @Ignore
    public void test_01() throws IOException {
        Modeling modeling = repository.
                save(Modeling.builder().name("Modeling Name - test 1").description(
                        "Modeling Description").build());
        Observation o = service.save(new Observation(modeling,
                "application/zip",
                "bear.zip",
                "my label"), this.getClass().getResourceAsStream("/animals/bear.zip"));
        service.delete(o);
    }

    @Test
    @Ignore
    public void test_02() throws IOException {
        Modeling modeling = repository.
                save(Modeling.builder().name("Modeling Name - test 2").description(
                        "Modeling Description").build());
        Observation o1 = service.save(new Observation(modeling,
                "application/zip",
                "bear.zip",
                "bear"), this.getClass().getResourceAsStream("/animals/bear.zip"));
        Observation o2 = service.save(new Observation(modeling,
                "application/zip",
                "turtle.zip",
                "turtle"), this.getClass().getResourceAsStream("/animals/turtle.zip"));
        service.deleteAll(modeling);
    }


}
