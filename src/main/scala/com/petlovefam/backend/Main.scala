package com.petlovefam.backend

import com.petlovefam.backend.db.{DBMigrator, DBSetup}
import com.petlovefam.backend.di.DependencyInjection
import com.petlovefam.backend.graphql.GraphQLAPI
import zio.*

import javax.sql.DataSource

object Main extends ZIOAppDefault:

  val main: Task[Unit] = {
    for
      datasource <- ZIO.service[DataSource]
      _ <- DBMigrator.migrate(datasource)
      graphQL <- ZIO.service[GraphQLAPI]
      _ <- graphQL.run()
    yield ()
  }.provideSomeLayer(DependencyInjection.liveGraphQL)
    .provideLayer(DBSetup.liveDataSourceLayer)

  override def run: ZIO[ZIOAppArgs & Scope, Any, Unit] =
    for
      n <- Console.printLine("Starting application...")
      _ <- main
    yield ()
