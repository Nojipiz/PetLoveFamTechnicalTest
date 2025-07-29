package com.petlovefam.backend.feature.veterinary_appointment.domain.entity

sealed trait AppointmentType(val displayName: String)

object AppointmentType:
  case object Consultation extends AppointmentType("Consultation")
  case object Vaccination extends AppointmentType("Vaccination")

  def fromString(value: String): AppointmentType = value match
    case "Consultation" => Consultation
    case _              => Vaccination
