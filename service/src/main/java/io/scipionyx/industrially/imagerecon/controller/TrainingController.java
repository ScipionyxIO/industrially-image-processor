package io.scipionyx.industrially.imagerecon.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@Slf4j
class TrainingController {

    @PutMapping(value = "/train")
    public String train() {
        return "Trained " + Calendar.getInstance().toString();
    }

}
