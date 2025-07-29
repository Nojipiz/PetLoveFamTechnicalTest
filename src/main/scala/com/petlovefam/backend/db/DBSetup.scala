package com.petlovefam.backend.db

import io.getquill.jdbczio.Quill
import io.getquill.{SnakeCase, SqliteJdbcContext}
import zio.*

import javax.sql.DataSource

object DBSetup:

  private def availableSourceSchedule(sourceName: String, sourcePath: Option[String] = None) = Schedule
    .fixed(2000.milliseconds)
    .tapOutput(o =>
      ZIO.logInfo(
        s"Waiting for $sourceName to be available ${sourcePath.map(path => s"at ${path}").getOrElse("")}, retry count: $o"
      )
    )

  val liveDataSourceLayer: ULayer[DataSource] = ZLayer.succeed {
    new SqliteJdbcContext(SnakeCase, "live-database").dataSource
  }

  val liveSqlLayer: ZLayer[DataSource, Throwable, Quill.Sqlite[SnakeCase]] =
    liveDataSourceLayer >>> Quill.Sqlite.fromNamingStrategy(SnakeCase)
