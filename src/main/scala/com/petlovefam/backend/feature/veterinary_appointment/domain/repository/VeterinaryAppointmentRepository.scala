package com.petlovefam.backend.feature.veterinary_appointment.domain.repository

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.VeterinaryAppointment
import java.time.LocalDateTime

trait VeterinaryAppointmentRepository[F[_]]:
  def insert(appointment: VeterinaryAppointment): F[VeterinaryAppointment]
  def findAll(): F[List[VeterinaryAppointment]]
  def findWithDateGreater(date: LocalDateTime): F[List[VeterinaryAppointment]]
