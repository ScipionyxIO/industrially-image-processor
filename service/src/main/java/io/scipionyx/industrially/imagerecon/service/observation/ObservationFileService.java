package io.scipionyx.industrially.imagerecon.service.observation;

import io.scipionyx.industrially.imagerecon.configuration.TrainingConfiguration;
import io.scipionyx.industrially.imagerecon.model.Modeling;
import io.scipionyx.industrially.imagerecon.model.Observation;
import io.scipionyx.industrially.imagerecon.repository.ObservationRepository;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
@ConditionalOnProperty(name = "scipionyx.useAws", havingValue = "false", matchIfMissing = true)
@Slf4j
public class ObservationFileService implements ObservationService {

    private final TrainingConfiguration configuration;

    private final ObservationRepository repository;

    @Autowired
    private ObservationFileService(TrainingConfiguration configuration,
                                   ObservationRepository repository) {
        this.configuration = configuration;
        this.repository = repository;
    }

    @PostConstruct
    public void init() throws IOException {
        Assert.notNull(configuration.getObservationFolder(),
                "Target Folder must be defined at: scipionyx.industrially.imagerecon.modeling.observationFolder");
        Assert.isTrue(configuration.getObservationFolder().getFile().exists(),
                "Target Folder must be defined at: scipionyx.industrially.imagerecon.modeling.observationFolder");
        Assert.isTrue(configuration.getObservationFolder().getFile().isDirectory(),
                "Target Folder must be defined at: scipionyx.industrially.imagerecon.modeling.observationFolder");
        Assert.notNull(configuration.getTrainingFolder(),
                "Training Folder must be defined at: scipionyx.industrially.imagerecon.modeling.TrainingFolder - NULL");
        Assert.isTrue(configuration.getTrainingFolder().getFile().isDirectory(),
                "Training Folder must be defined at: scipionyx.industrially.imagerecon.modeling.TrainingFolder");
    }

    @Override
    public Observation save(Observation observation,
                            InputStream inputStream) throws IOException {
        observation = repository.save(observation);
        FileUtils.copyInputStreamToFile(inputStream, targetFile(observation).get());
        return observation;
    }

    @Override
    public File load(Modeling modeling) throws IOException {
        repository.
                streamByModeling(modeling).
                map(this::loadFile).
                filter(Objects::nonNull).
                map(this::handle).
                filter(Try::isFailure).
                forEach(i -> log.info("Is Failure: {}, Cause: {}", i.isFailure(), i.getCause().toString()));
        return Paths.
                get(configuration.getTrainingFolder().getFile().toString(), modeling.getId().toString()).toFile();
    }

    private Try<Observation> handle(Observation observation) {
        log.info("Handling Observation: {}, is file null: {}", observation.getId(), observation.getFile() == null);
        return Try.of(() -> {
            int BUFFER = 2048;
            Path folder = Paths.get(configuration.getTrainingFolder().getFile().toString(),
                    observation.getModeling().getId().toString(),
                    observation.getLabel());
            if (!folder.toFile().exists() &&
                    !folder.toFile().mkdirs()) {
                log.error("Folder was not created");
            }
            Assert.notNull(observation.getFile(), "File should be available");
            ZipFile zipFile = new ZipFile(observation.getFile());
            Enumeration zipFileEntries = zipFile.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                log.debug("unzipping file: {}", currentEntry);
                File destFile = new File(folder.toString(), currentEntry);
                File destinationParent = destFile.getParentFile();
                if (!destinationParent.exists()) {
                    Assert.isTrue(destinationParent.mkdirs(), "Folder must be created");
                }
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos,
                            BUFFER);
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
            return observation;
        });
    }

    @Override
    public void deleteAll(Modeling modeling) throws IOException {
        repository.
                streamByModeling(modeling).
                forEach(this::delete);
        log.debug("Deleting Root path:{}, and Folder:{}",
                configuration.getTrainingFolder().getFile().toString(),
                modeling.getId());
        FileUtils.deleteDirectory(Paths.get(configuration.getTrainingFolder().getFile().toString(),
                modeling.getId().toString()).toFile());
    }

    @Override
    public Try<Observation> delete(Observation observation) {
        return Try.of(() -> {
            FileUtils.forceDeleteOnExit(targetFile(observation).get());
            repository.delete(observation);
            return observation;
        });
    }

    private Observation loadFile(Observation observation) {
        log.info("Loading Observation: {}", observation.getId());
        observation.setFile(targetFile(observation).get());
        return observation.getFile() != null ? observation : null;
    }

    private Try<File> targetFile(Observation observation) {
        return Try.of(() -> Paths.get(
                configuration.getObservationFolder().getFile().toString(),
                observation.getId() + ".observation").toFile());
    }


}
