package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.repository.dao

import io.github.arainko.ducktape.*
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.*
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

case class VeterinaryAppointmentDAO(
    id: String,
    petId: String,
    date: String,
    `type`: AppointmentType
):
  def toDomain = this
    .into[VeterinaryAppointment]
    .transform(Field.computed(_.date, d => LocalDateTime.parse(d.date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)))

object VeterinaryAppointmentDAO:
  def fromDomain(entity: VeterinaryAppointment) = entity
    .into[VeterinaryAppointmentDAO]
    .transform(Field.computed(_.date, _.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
