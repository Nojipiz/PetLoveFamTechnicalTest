package com.petlovefam.backend.db

import com.petlovefam.backend.config.Config.DBConfig
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.{Location, MigrationState}
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.output.MigrateResult
import zio.*
import zio.logging.*

import javax.sql.DataSource
import scala.annotation.migration
import scala.jdk.CollectionConverters.*

object DBMigrator:

  def migrate(datasource: DataSource): Task[Unit] =
    ZIO
      .attempt(Flyway.configure().dataSource(datasource).load().migrate())
      .flatMap {
        case r: MigrateResult =>
          ZIO.succeed(())
        case null =>
          ZIO.fail(new Throwable("Something goes wrong in database migraton"))
      }
      .onError(cause => ZIO.logErrorCause("Database migration has failed", cause))
