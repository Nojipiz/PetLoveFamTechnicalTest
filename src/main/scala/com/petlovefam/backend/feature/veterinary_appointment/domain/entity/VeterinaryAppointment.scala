package com.petlovefam.backend.feature.veterinary_appointment.domain.entity

case class VeterinaryAppointment(
    id: String,
    petId: String,
    date: String,
    `type`: AppointmentType
)
