package com.petlovefam.backend.feature.pet_owner.infrastructure.route

import com.petlovefam.backend.feature.pet_owner.application.PetOwnerService
import com.petlovefam.graphql.Types
import zio.*
import com.petlovefam.backend.feature.pet_owner.domain.entity.PetOwner
import io.github.arainko.ducktape.*

class PetOwnerRoutes(petOwnerService: PetOwnerService):

  def getPetOwners(): Task[List[Types.PetOwner]] =
    petOwnerService.getPetOwners.map(_.map(_.into[Types.PetOwner].transform()))

  def createPetOwner(createPetOwnerArgs: Types.MutationCreatePetOwnerArgs): Task[Types.PetOwner] =
    petOwnerService
      .createPetOwner(name = createPetOwnerArgs.name, email = createPetOwnerArgs.email)
      .map(_.into[Types.PetOwner].transform())

object PetOwnerRoutes:
  def live: ZLayer[PetOwnerService, Nothing, PetOwnerRoutes] =
    ZLayer.fromFunction(PetOwnerRoutes(_))
