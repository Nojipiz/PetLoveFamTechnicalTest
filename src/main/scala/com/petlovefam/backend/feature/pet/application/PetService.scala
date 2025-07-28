package com.petlovefam.backend.feature.pet.application

import com.petlovefam.backend.feature.pet.domain.entity.Pet
import com.petlovefam.backend.feature.pet.domain.repository.PetRepository
import zio.{Task, ZIO, ZLayer}
import java.util.UUID

class PetService private (petRepository: PetRepository[Task]) {

  def getPets: Task[List[Pet]] = petRepository.findAll()

  def createPet(name: String, breed: String, birthDate: String, picture: Option[String] = None): Task[Pet] =
    val pet = Pet(
      id = UUID.randomUUID().toString(),
      name = name,
      breed = breed,
      birthDate = birthDate,
      picture = picture
    )
    petRepository.insert(pet)
}

object PetService {
  val live: ZLayer[PetRepository[Task], Nothing, PetService] =
    ZLayer.fromFunction(new PetService(_))
}
