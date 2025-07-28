package com.petlovefam.backend.di

import com.petlovefam.backend.db.DBSetup
import com.petlovefam.backend.feature.pet_owner.application.PetOwnerService
import com.petlovefam.backend.feature.pet_owner.infrastructure.repository.PetOwnerRepositoryImpl
import com.petlovefam.backend.feature.pet.application.PetService
import com.petlovefam.backend.feature.pet.infrastructure.repository.PetRepositoryImpl
import com.petlovefam.backend.feature.veterinary_appointment.application.VeterinaryAppointmentService
import com.petlovefam.backend.feature.veterinary_appointment.infrastructure.repository.VeterinaryAppointmentRepositoryImpl
import com.petlovefam.backend.graphql.GraphQLAPI
import zio.ZLayer

import javax.sql.DataSource
import com.petlovefam.backend.feature.pet_owner.infrastructure.route.PetOwnerRoutes
import com.petlovefam.backend.feature.pet.infrastructure.route.PetRoutes
import com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route.VeterinaryAppointmentRoutes

object DependencyInjection:

  type Routes = PetOwnerRoutes & PetRoutes & VeterinaryAppointmentRoutes

  type Services = PetOwnerService & PetService & VeterinaryAppointmentService

  val liveRepositories: ZLayer[
    DataSource,
    Throwable,
    PetOwnerRepositoryImpl & PetRepositoryImpl & VeterinaryAppointmentRepositoryImpl
  ] =
    DBSetup.liveSqlLayer >>> (PetOwnerRepositoryImpl.live ++ PetRepositoryImpl.live ++ VeterinaryAppointmentRepositoryImpl.live)

  val liveServices = liveRepositories >>> (PetOwnerService.live ++ PetService.live ++ VeterinaryAppointmentService.live)

  val liveRoutes = liveServices >>> (PetOwnerRoutes.live ++ PetRoutes.live ++ VeterinaryAppointmentRoutes.live)

  val liveGraphQL = liveRoutes >>> GraphQLAPI.live
