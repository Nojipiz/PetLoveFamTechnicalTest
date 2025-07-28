package com.petlovefam.backend.feature.pet.infrastructure.route

import com.petlovefam.backend.feature.pet.application.PetService
import com.petlovefam.graphql.Types
import zio.*
import com.petlovefam.backend.feature.pet.domain.entity.Pet
import io.github.arainko.ducktape.*

class PetRoutes(petService: PetService):

  def getPets(): Task[List[Types.Pet]] =
    petService.getPets.map(_.map(_.into[Types.Pet].transform()))

  def createPet(createPetArgs: Types.MutationCreatePetArgs): Task[Types.Pet] =
    petService
      .createPet(
        name = createPetArgs.name,
        breed = createPetArgs.breed,
        birthDate = createPetArgs.birthDate,
        picture = createPetArgs.picture
      )
      .map(_.into[Types.Pet].transform())

object PetRoutes:
  def live: ZLayer[PetService, Nothing, PetRoutes] =
    ZLayer.fromFunction(PetRoutes(_))
