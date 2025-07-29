package com.petlovefam.backend.feature.pet_owner.infrastructure.repository

import com.petlovefam.backend.feature.pet_owner.domain.entity.PetOwner
import com.petlovefam.backend.feature.pet_owner.domain.repository.PetOwnerRepository
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.{Task, ZLayer}

class PetOwnerRepositoryImpl(
    quill: Quill.Sqlite[SnakeCase]
) extends PetOwnerRepository[Task]:
  import quill.*

  override def findAll(): Task[List[PetOwner]] = run(query[PetOwner])

  override def insert(petOwner: PetOwner): Task[PetOwner] = run(
    query[PetOwner].insertValue(lift(petOwner))
  ).as(petOwner)

  override def findById(petOwnerId: String): Task[Option[PetOwner]] = run(
    query[PetOwner].filter(_.id == lift(petOwnerId)).take(1)
  ).map(_.headOption)

object PetOwnerRepositoryImpl:
  val live = ZLayer.fromFunction(new PetOwnerRepositoryImpl(_))
