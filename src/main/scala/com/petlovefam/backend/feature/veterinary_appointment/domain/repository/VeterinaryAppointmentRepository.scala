package com.petlovefam.backend.feature.veterinary_appointment.domain.repository

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.VeterinaryAppointment

trait VeterinaryAppointmentRepository[F[_]]:
  def insert(appointment: VeterinaryAppointment): F[VeterinaryAppointment]
  def findAll(): F[List[VeterinaryAppointment]]
