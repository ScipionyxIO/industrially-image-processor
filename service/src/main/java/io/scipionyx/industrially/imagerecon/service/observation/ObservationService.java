package io.scipionyx.industrially.imagerecon.service.observation;

import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import io.vavr.control.Try;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ObservationService {

    Observation save(Observation observation, InputStream inputStream) throws IOException;

    File load(Modeling modeling) throws IOException;

    void deleteAll(Modeling modeling) throws IOException;

    Try<Observation> delete(Observation observation);
}
