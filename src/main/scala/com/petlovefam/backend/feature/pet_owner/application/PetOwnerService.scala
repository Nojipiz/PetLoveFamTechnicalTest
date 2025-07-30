package com.petlovefam.backend.feature.pet_owner.application

import com.petlovefam.backend.feature.pet_owner.domain.entity.PetOwner
import com.petlovefam.backend.feature.pet_owner.domain.repository.PetOwnerRepository
import zio.{Task, ZIO, ZLayer}
import java.util.UUID

class PetOwnerService private (petOwnerRepository: PetOwnerRepository[Task]):

  def getPetOwners: Task[List[PetOwner]] = petOwnerRepository.findAll()

  def getPetOwnerById(petOwnerId: String): Task[Option[PetOwner]] = petOwnerRepository.findById(petOwnerId)

  def createPetOwner(name: String, email: String): Task[PetOwner] =
    val petOwner = PetOwner(
      id = UUID.randomUUID().toString(),
      name = name,
      email = email
    )
    petOwnerRepository.insert(petOwner)

object PetOwnerService:
  val live: ZLayer[PetOwnerRepository[Task], Nothing, PetOwnerService] =
    ZLayer.fromFunction(new PetOwnerService(_))
