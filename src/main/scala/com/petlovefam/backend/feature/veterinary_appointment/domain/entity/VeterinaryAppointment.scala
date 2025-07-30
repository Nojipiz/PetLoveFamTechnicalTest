package com.petlovefam.backend.feature.veterinary_appointment.domain.entity

import java.time.LocalDateTime

case class VeterinaryAppointment(
    id: String,
    petId: String,
    date: LocalDateTime,
    `type`: AppointmentType
)
