package com.terrence;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TestcontainersTest extends AbstractTestcontainers {
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();

    }
}