package com.petlovefam.backend.feature.pet.infrastructure.route.mapper

import com.petlovefam.graphql.Types
import com.petlovefam.backend.feature.pet.domain.entity.Pet
import io.github.arainko.ducktape.*
import zio.Task
import com.petlovefam.backend.feature.pet_owner.domain.entity.PetOwner

private[route] object Mapper:

  def toGraphQL(pet: Pet, owner: Task[Types.PetOwner]): Types.Pet =
    pet
      .into[Types.Pet]
      .transform(Field.const(_.owner, owner))

  def toGraphQL(petOwner: PetOwner): Types.PetOwner =
    petOwner
      .into[Types.PetOwner]
      .transform()
