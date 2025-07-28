package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route

import com.petlovefam.backend.feature.veterinary_appointment.application.VeterinaryAppointmentService
import com.petlovefam.graphql.Types
import zio.*
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.VeterinaryAppointment
import com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route.mapper.Mapper

class VeterinaryAppointmentRoutes(appointmentService: VeterinaryAppointmentService):

  def getVeterinaryAppointments(
      queryVeterinaryAppointmentArgs: Types.QueryVeterinaryAppointmentsArgs
  ): Task[List[Types.VeterinaryAppointment]] =
    appointmentService.getAppointments.map(_.map(Mapper.toGraphQL))

  def createVeterinaryAppointment(
      createAppointmentArgs: Types.MutationCreateVeterinaryAppointmentArgs
  ): Task[Types.VeterinaryAppointment] =
    appointmentService
      .createAppointment(
        petId = createAppointmentArgs.petId,
        date = createAppointmentArgs.date,
        appointmentType = Mapper.fromGraphQL(createAppointmentArgs.`type`)
      )
      .map(Mapper.toGraphQL)

object VeterinaryAppointmentRoutes:
  def live: ZLayer[VeterinaryAppointmentService, Nothing, VeterinaryAppointmentRoutes] =
    ZLayer.fromFunction(VeterinaryAppointmentRoutes(_))
