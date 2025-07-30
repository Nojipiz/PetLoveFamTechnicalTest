package com.petlovefam.backend.feature.pet.application

import com.petlovefam.backend.feature.pet.domain.entity.Pet
import com.petlovefam.backend.feature.pet.domain.repository.PetRepository
import com.petlovefam.backend.feature.pet.domain.error.PetDomainError
import com.petlovefam.backend.feature.pet_owner.domain.repository.PetOwnerRepository
import com.petlovefam.backend.feature.pet_owner.domain.error.PetOwnerDomainError
import zio.{Task, ZIO, ZLayer}
import java.util.UUID
import com.petlovefam.backend.feature.pet.application.request.CreatePetRequest

class PetService private (petRepository: PetRepository[Task], petOwnerRepository: PetOwnerRepository[Task]):

  def getPets: Task[List[Pet]] = petRepository.findAll()

  def getPetById(petId: String): Task[Option[Pet]] = petRepository.findById(petId)

  def createPet(createPetRequest: CreatePetRequest): Task[Pet] =
    for
      _ <- petOwnerRepository
        .findById(createPetRequest.petOwnerId)
        .someOrElse(PetOwnerDomainError.PetOwnerNotFound(petOwnerId = createPetRequest.petOwnerId))
      pet = Pet(
        id = UUID.randomUUID().toString(),
        petOwnerId = createPetRequest.petOwnerId,
        name = createPetRequest.name,
        breed = createPetRequest.breed,
        birthDate = createPetRequest.birthDate,
        pictureUrl = createPetRequest.pictureUrl
      )
      result <- petRepository.insert(pet)
    yield result

object PetService:
  val live: ZLayer[PetRepository[Task] & PetOwnerRepository[Task], Nothing, PetService] =
    ZLayer.fromFunction(new PetService(_, _))
