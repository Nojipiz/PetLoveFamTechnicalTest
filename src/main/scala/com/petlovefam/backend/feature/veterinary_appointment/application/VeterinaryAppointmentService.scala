package com.petlovefam.backend.feature.veterinary_appointment.application

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.{VeterinaryAppointment, AppointmentType}
import com.petlovefam.backend.feature.veterinary_appointment.domain.repository.VeterinaryAppointmentRepository
import zio.{Task, ZIO, ZLayer}
import java.util.UUID

class VeterinaryAppointmentService private (appointmentRepository: VeterinaryAppointmentRepository[Task]):

  def getAppointments: Task[List[VeterinaryAppointment]] = appointmentRepository.findAll()

  def createAppointment(petId: String, date: String, appointmentType: AppointmentType): Task[VeterinaryAppointment] =
    val appointment = VeterinaryAppointment(
      id = UUID.randomUUID().toString(),
      petId = petId,
      date = date,
      `type` = appointmentType
    )
    appointmentRepository.insert(appointment)

object VeterinaryAppointmentService:
  val live: ZLayer[VeterinaryAppointmentRepository[Task], Nothing, VeterinaryAppointmentService] =
    ZLayer.fromFunction(new VeterinaryAppointmentService(_))
