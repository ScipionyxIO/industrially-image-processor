package io.scipionyx.industrially.imagerecon.repository;

import io.scipionyx.industrially.imagerecon.Application;
import io.scipionyx.industrially.imagerecon.model.Modeling;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Slf4j
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@Rollback
@Transactional
public class TrainingRepositoryTest {

    @Autowired
    private ModelingRepository modelingRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    public void test_01() {
        Modeling modeling = modelingRepository.
                save(Modeling.builder().name("Modeling Name - test 1").description(
                        "Modeling Description").build());
        Assert.assertNotNull("Training Id must be Not null after save", modeling.getId());
//        trainingRepository.save(Training.(modeling,
//                ModelType.ALEX_NET,
//                1234l,
//                10, 10,
//                10,
//                true, true,
//                1, 1, 1));
    }

}
