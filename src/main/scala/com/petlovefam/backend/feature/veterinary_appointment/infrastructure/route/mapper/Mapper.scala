package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route.mapper

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity._
import com.petlovefam.graphql.Types
import io.github.arainko.ducktape.*

private[route] object Mapper:
  def fromGraphQL(appointmentType: Types.AppointmentType): AppointmentType = appointmentType match {
    case Types.AppointmentType.VACCINATION  => AppointmentType.Vaccination
    case Types.AppointmentType.CONSULTATION => AppointmentType.Consultation
  }

  def toGraphQL(appointmentType: AppointmentType): Types.AppointmentType = appointmentType match {
    case AppointmentType.Vaccination  => Types.AppointmentType.VACCINATION
    case AppointmentType.Consultation => Types.AppointmentType.CONSULTATION
  }

  def toGraphQL(veterinaryAppointment: VeterinaryAppointment): Types.VeterinaryAppointment = {
    veterinaryAppointment
      .into[Types.VeterinaryAppointment]
      .transform(Field.computed(_.`type`, c => Types.AppointmentType.VACCINATION))
  }
