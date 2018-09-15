package io.scipionyx.industrially.imagerecon.controller;

import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import io.scipionyx.industrially.imagerecon.repository.ModelingRepository;
import io.scipionyx.industrially.imagerecon.service.observation.ObservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/service/")
@Slf4j
class ObservationController {

    private final ObservationService service;

    private final ModelingRepository repository;

    @Autowired
    private ObservationController(ObservationService service, ModelingRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PutMapping(value = "/observation/{trainingId}")
    public String upload(@PathVariable long modellingId,
                         @RequestParam(name = "compressed",
                                 defaultValue = "false",
                                 required = false) boolean compressed,
                         @RequestParam(name = "label") String label,
                         @RequestParam(name = "file") MultipartFile file) throws IOException {
        Modeling modeling = repository.findById(modellingId).orElse(null);
        Assert.notNull(modeling, "Modeling id not found");
        service.save(
                new Observation(modeling,
                        file.getContentType(),
                        file.getOriginalFilename(),
                        label)
                , file.getInputStream());
        return "File [" + file.getName() + "] uploaded successfully";
    }

}
