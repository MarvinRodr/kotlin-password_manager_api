package com.marvinrodr.password.infrastructure.persistence

import com.marvinrodr.common.Either
import com.marvinrodr.common.Left
import com.marvinrodr.common.Right
import com.marvinrodr.password.domain.*
import com.marvinrodr.password.domain.customErrors.PasswordFindError
import com.marvinrodr.password.domain.customErrors.PasswordNotFoundError
import java.sql.ResultSet
import java.util.UUID
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgrePasswordRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : PasswordRepository {

    override fun save(password: Password) {
        MapSqlParameterSource()
            .addValue("id", password.id.value.toString())
            .addValue("name", password.name.value)
            .addValue("secretKey", password.secretKey.value)
            .addValue("createdAt", password.createdAt)
            .let { params ->
                jdbcTemplate.update(
                    "INSERT INTO password (id, name, secret_key, created_at) VALUES (:id,:name, :secretKey, :createdAt)",
                    params
                )
            }
    }

    override fun find(id: PasswordId): Either<PasswordFindError, Password> = try {
        val query = "SELECT * FROM password where id=:id"
        val params = MapSqlParameterSource().addValue("id", id.value.toString())

        jdbcTemplate.queryForObject(query, params, mapRow())
            ?.let { Right(it) }
            ?: Left(PasswordNotFoundError(id))

    } catch (exception: Throwable) {
        Left(PasswordNotFoundError(id))
    }

    override fun delete(id: PasswordId) {
        MapSqlParameterSource()
            .addValue("id", id.value.toString())
            .let { params ->
                jdbcTemplate.update(
                    "DELETE FROM password WHERE id = :id",
                    params
                )
            }
    }

    private fun mapRow(): RowMapper<Password> {
        return RowMapper { rs: ResultSet, _: Int ->
            val id = PasswordId(UUID.fromString(rs.getString("id")))
            val name = PasswordName(rs.getString("name"))
            val secretKey = PasswordSecretKey(rs.getString("secret_key"))
            val createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            Password(id, name, secretKey, createdAt, listOf())
        }
    }
}
