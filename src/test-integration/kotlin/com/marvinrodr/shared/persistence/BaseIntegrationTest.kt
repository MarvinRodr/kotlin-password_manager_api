package com.marvinrodr.shared.persistence

import com.marvinrodr.config.DatabaseConfig
import com.marvinrodr.shared.Application
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [Application::class])
@Import(DatabaseConfig::class)
@ActiveProfiles("test")
class BaseIntegrationTest {

    init {
        postgresContainer.start()
    }
    companion object {
        @JvmStatic
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14.5")
            .withDatabaseName("password_database")
            .withUsername("password_username")
            .withPassword("password_password")
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}