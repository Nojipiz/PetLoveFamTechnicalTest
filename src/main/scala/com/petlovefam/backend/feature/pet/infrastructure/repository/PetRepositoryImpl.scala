package com.petlovefam.backend.feature.pet.infrastructure.repository

import com.petlovefam.backend.feature.pet.domain.entity.Pet
import com.petlovefam.backend.feature.pet.domain.repository.PetRepository
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.{Task, ZLayer}
import zio.query.*
import zio.Chunk
import zio.ZIO
import zio.Trace

class PetRepositoryImpl(
    quill: Quill.Sqlite[SnakeCase]
) extends PetRepository[Task]:
  import quill.*

  override def findAll(): Task[List[Pet]] = run(query[Pet])

  override def insert(pet: Pet): Task[Pet] = run(
    query[Pet].insertValue(lift(pet))
  ).as(pet)

  override def findById(id: String): Task[Option[Pet]] = run(
    query[Pet].filter(_.id == lift(id))
  ).map(_.headOption)

object PetRepositoryImpl:
  val live = ZLayer.fromFunction(new PetRepositoryImpl(_))
