package com.petlovefam.backend.feature.pet_owner.domain.repository

import com.petlovefam.backend.feature.pet_owner.domain.entity.PetOwner

trait PetOwnerRepository[F[_]] {
  def insert(petOwner: PetOwner): F[PetOwner]
  def findAll(): F[List[PetOwner]]
  def findById(petOwnerId: String): F[Option[PetOwner]]
}
