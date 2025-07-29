package com.petlovefam.backend.feature.pet.infrastructure.route

import com.petlovefam.backend.feature.pet.application.PetService
import com.petlovefam.graphql.Types
import zio.*
import com.petlovefam.backend.feature.pet.domain.entity.Pet
import com.petlovefam.backend.feature.pet.application.request.CreatePetRequest
import com.petlovefam.backend.feature.pet.infrastructure.route.mapper.Mapper
import com.petlovefam.backend.feature.pet_owner.application.PetOwnerService
import io.github.arainko.ducktape.*

class PetRoutes(petService: PetService, petOwnerService: PetOwnerService):

  def getPets(queryPetsArgs: Types.QueryPetsArgs): Task[List[Types.Pet]] =
    for
      allPets <- petService.getPets
      pets = allPets.map { pet =>
        Mapper.toGraphQL(
          pet,
          petOwnerService
            .getPetOwner(petOwnerId = pet.petOwnerId)
            .someOrFailException
            .map(Mapper.toGraphQL)
        )
      }
    yield pets

  def createPet(createPetArgs: Types.MutationCreatePetArgs): Task[Types.Pet] =
    for
      createdPet <- petService.createPet(createPetArgs.into[CreatePetRequest].transform())
      pet = Mapper.toGraphQL(
        createdPet,
        petOwnerService
          .getPetOwner(petOwnerId = createdPet.petOwnerId)
          .someOrFailException
          .map(Mapper.toGraphQL)
      )
    yield pet

object PetRoutes:
  def live: ZLayer[PetService & PetOwnerService, Nothing, PetRoutes] =
    ZLayer.fromFunction(PetRoutes(_, _))
