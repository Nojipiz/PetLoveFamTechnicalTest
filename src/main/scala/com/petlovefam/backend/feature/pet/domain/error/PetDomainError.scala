package com.petlovefam.backend.feature.pet.domain.error

sealed trait PetDomainError extends Throwable:
  def message: String
  override def getMessage: String = message

object PetDomainError:
  case class PetNotFound(petId: String) extends PetDomainError:
    override val message: String = s"Pet with id '$petId' not found"
