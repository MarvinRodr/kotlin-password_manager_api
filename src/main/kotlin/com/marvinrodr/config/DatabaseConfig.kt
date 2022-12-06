package com.marvinrodr.config

import com.marvinrodr.password.infrastructure.persistence.PostgrePasswordRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class DatabaseConfig {

    @Bean
    fun passwordRepository(jdbcTemplate: NamedParameterJdbcTemplate) = PostgrePasswordRepository(jdbcTemplate)
}