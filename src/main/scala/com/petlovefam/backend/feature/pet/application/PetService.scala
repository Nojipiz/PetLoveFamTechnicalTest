package com.petlovefam.backend.feature.pet.application

import com.petlovefam.backend.feature.pet.domain.entity.Pet
import com.petlovefam.backend.feature.pet.domain.repository.PetRepository
import zio.{Task, ZIO, ZLayer}
import java.util.UUID
import com.petlovefam.backend.feature.pet.application.request.CreatePetRequest

class PetService private (petRepository: PetRepository[Task]):

  def getPets: Task[List[Pet]] = petRepository.findAll()

  def createPet(createPetRequest: CreatePetRequest): Task[Pet] =
    val pet = Pet(
      id = UUID.randomUUID().toString(),
      petOwnerId = createPetRequest.petOwnerId,
      name = createPetRequest.name,
      breed = createPetRequest.breed,
      birthDate = createPetRequest.birthDate,
      pictureUrl = createPetRequest.pictureUrl
    )
    petRepository.insert(pet)

object PetService:
  val live: ZLayer[PetRepository[Task], Nothing, PetService] =
    ZLayer.fromFunction(new PetService(_))
