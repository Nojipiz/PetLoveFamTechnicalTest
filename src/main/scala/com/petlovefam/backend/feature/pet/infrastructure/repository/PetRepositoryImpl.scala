package com.petlovefam.backend.feature.pet.infrastructure.repository

import com.petlovefam.backend.feature.pet.domain.entity.Pet
import com.petlovefam.backend.feature.pet.domain.repository.PetRepository
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.{Task, ZLayer}

class PetRepositoryImpl(
    quill: Quill.Sqlite[SnakeCase]
) extends PetRepository[Task] {
  import quill.*

  override def findAll(): Task[List[Pet]] = run(query[Pet])

  override def insert(pet: Pet): Task[Pet] = run(
    query[Pet].insertValue(lift(pet))
  ).as(pet)
}

object PetRepositoryImpl {
  val live = ZLayer.fromFunction(new PetRepositoryImpl(_))
}
