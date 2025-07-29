package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.repository

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.VeterinaryAppointment
import com.petlovefam.backend.feature.veterinary_appointment.domain.repository.VeterinaryAppointmentRepository
import io.getquill.*
import io.getquill.autoQuote
import io.getquill.jdbczio.Quill
import zio.{Task, ZLayer}
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.AppointmentType

class VeterinaryAppointmentRepositoryImpl(
    quill: Quill.Sqlite[SnakeCase]
) extends VeterinaryAppointmentRepository[Task]:
  import quill.*

  implicit val encodeType: MappedEncoding[AppointmentType, String] =
    MappedEncoding[AppointmentType, String](_.displayName)
  implicit val decodeType: MappedEncoding[String, AppointmentType] =
    MappedEncoding[String, AppointmentType](AppointmentType.fromString(_))

  override def findAll(): Task[List[VeterinaryAppointment]] = run(query[VeterinaryAppointment])

  override def insert(appointment: VeterinaryAppointment): Task[VeterinaryAppointment] = run(
    query[VeterinaryAppointment].insertValue(lift(appointment))
  ).as(appointment)

object VeterinaryAppointmentRepositoryImpl:

  val live = ZLayer.fromFunction(new VeterinaryAppointmentRepositoryImpl(_))
