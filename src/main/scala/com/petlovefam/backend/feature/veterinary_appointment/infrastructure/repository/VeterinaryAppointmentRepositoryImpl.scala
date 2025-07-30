package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.repository

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.VeterinaryAppointment
import com.petlovefam.backend.feature.veterinary_appointment.domain.repository.VeterinaryAppointmentRepository
import io.getquill.*
import io.getquill.autoQuote
import io.getquill.jdbczio.Quill
import zio.{Task, ZLayer}
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.AppointmentType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.petlovefam.backend.feature.veterinary_appointment.infrastructure.repository.dao.VeterinaryAppointmentDAO

class VeterinaryAppointmentRepositoryImpl private (
    quill: Quill.Sqlite[SnakeCase]
) extends VeterinaryAppointmentRepository[Task]:
  import VeterinaryAppointmentRepositoryImpl.implicits.given
  import quill.*
  val veterinaryAppointments = quote {
    querySchema[VeterinaryAppointmentDAO](
      "veterinary_appointment"
    )
  }

  override def findAll(): Task[List[VeterinaryAppointment]] =
    run(veterinaryAppointments).map(_.map(_.toDomain))

  override def insert(appointment: VeterinaryAppointment): Task[VeterinaryAppointment] = run(
    veterinaryAppointments.insertValue(lift(VeterinaryAppointmentDAO.fromDomain(appointment)))
  ).as(appointment)

  override def findWithDateGreater(date: LocalDateTime): Task[List[VeterinaryAppointment]] =
    val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    run(veterinaryAppointments.filter(v => infix"${v.date} > ${lift(dateString)}".as[Boolean]))
      .map(_.map(_.toDomain))

object VeterinaryAppointmentRepositoryImpl:

  val live = ZLayer.fromFunction(new VeterinaryAppointmentRepositoryImpl(_))

  object implicits:
    given MappedEncoding[AppointmentType, String] =
      MappedEncoding[AppointmentType, String](_.displayName)
    given MappedEncoding[String, AppointmentType] =
      MappedEncoding[String, AppointmentType](AppointmentType.fromString(_))
