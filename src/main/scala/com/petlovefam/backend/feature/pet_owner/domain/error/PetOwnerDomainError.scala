package com.petlovefam.backend.feature.pet_owner.domain.error

sealed trait PetOwnerDomainError extends Throwable:
  def message: String
  override def getMessage: String = message

object PetOwnerDomainError:
  case class PetOwnerNotFound(petOwnerId: String) extends PetOwnerDomainError:
    override val message: String = s"Pet owner with id '$petOwnerId' not found"
