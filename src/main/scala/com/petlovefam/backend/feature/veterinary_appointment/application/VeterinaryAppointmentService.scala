package com.petlovefam.backend.feature.veterinary_appointment.application

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.{VeterinaryAppointment, AppointmentType}
import com.petlovefam.backend.feature.pet.domain.error.*
import com.petlovefam.backend.feature.veterinary_appointment.domain.repository.VeterinaryAppointmentRepository
import zio.{Task, ZIO, ZLayer}
import java.util.UUID
import com.petlovefam.backend.feature.pet.domain.repository.PetRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VeterinaryAppointmentService private (
    appointmentRepository: VeterinaryAppointmentRepository[Task],
    petRepository: PetRepository[Task]
):

  def getAppointments(incommingOnly: Boolean): Task[List[VeterinaryAppointment]] = incommingOnly match
    case true  => appointmentRepository.findWithDateGreater(LocalDateTime.now())
    case false => appointmentRepository.findAll()

  def createAppointment(petId: String, rawDate: String, appointmentType: AppointmentType): Task[VeterinaryAppointment] =
    for
      _ <- petRepository.findById(petId).someOrFail(PetDomainError.PetNotFound(petId = petId))
      date <- ZIO.attempt {
        LocalDateTime.parse(rawDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      }
      appointment = VeterinaryAppointment(
        id = UUID.randomUUID().toString(),
        petId = petId,
        date = date,
        `type` = appointmentType
      )
      result <- appointmentRepository.insert(appointment)
    yield result

object VeterinaryAppointmentService:
  val live: ZLayer[VeterinaryAppointmentRepository[Task] & PetRepository[Task], Nothing, VeterinaryAppointmentService] =
    ZLayer.fromFunction(new VeterinaryAppointmentService(_, _))
