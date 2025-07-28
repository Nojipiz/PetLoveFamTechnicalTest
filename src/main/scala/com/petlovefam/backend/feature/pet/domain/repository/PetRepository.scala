package com.petlovefam.backend.feature.pet.domain.repository

import com.petlovefam.backend.feature.pet.domain.entity.Pet

trait PetRepository[F[_]] {
  def insert(pet: Pet): F[Pet]
  def findAll(): F[List[Pet]]
}
