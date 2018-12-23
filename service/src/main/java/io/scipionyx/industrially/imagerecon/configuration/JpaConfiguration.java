package io.scipionyx.industrially.imagerecon.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("io.scipionyx.industrially.imagerecon.model")
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"io.scipionyx.industrially.imagerecon.repository"})
public class JpaConfiguration {
}
