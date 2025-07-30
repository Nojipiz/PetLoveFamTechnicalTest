package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route

import com.petlovefam.backend.feature.veterinary_appointment.application.VeterinaryAppointmentService
import com.petlovefam.graphql.Types
import zio.*
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.VeterinaryAppointment
import com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route.mapper.Mapper
import com.petlovefam.backend.feature.pet.application.PetService
import com.petlovefam.backend.feature.pet_owner.application.PetOwnerService

class VeterinaryAppointmentRoutes(
    appointmentService: VeterinaryAppointmentService,
    petService: PetService,
    petOwnerService: PetOwnerService
):

  def getVeterinaryAppointments(
      queryVeterinaryAppointmentArgs: Types.QueryVeterinaryAppointmentsArgs
  ): Task[List[Types.VeterinaryAppointment]] =
    val getIncommingOnly = queryVeterinaryAppointmentArgs.incommingOnly.getOrElse(false)
    appointmentService
      .getAppointments(getIncommingOnly)
      .map(_.map(transformToGraphQL))

  def createVeterinaryAppointment(
      createAppointmentArgs: Types.MutationCreateVeterinaryAppointmentArgs
  ): Task[Types.VeterinaryAppointment] =
    appointmentService
      .createAppointment(
        petId = createAppointmentArgs.petId,
        rawDate = createAppointmentArgs.date,
        appointmentType = Mapper.fromGraphQL(createAppointmentArgs.`type`)
      )
      .map(transformToGraphQL)

  private def transformToGraphQL(veterinaryAppointment: VeterinaryAppointment): Types.VeterinaryAppointment =
    val pet = petService
      .getPetById(veterinaryAppointment.petId)
      .someOrFailException
      .map { pet =>
        val petOwner = petOwnerService
          .getPetOwnerById(petOwnerId = pet.petOwnerId)
          .someOrFailException
          .map(Mapper.toGraphQL)
        Mapper.toGraphQL(pet, petOwner)
      }
    Mapper.toGraphQL(veterinaryAppointment, pet)

object VeterinaryAppointmentRoutes:
  def live: ZLayer[VeterinaryAppointmentService & PetOwnerService & PetService, Nothing, VeterinaryAppointmentRoutes] =
    ZLayer.fromFunction(VeterinaryAppointmentRoutes(_, _, _))
