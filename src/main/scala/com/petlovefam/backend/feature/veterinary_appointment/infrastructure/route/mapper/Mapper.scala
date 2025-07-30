package com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route.mapper

import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.*
import com.petlovefam.backend.feature.pet.domain.entity.*
import com.petlovefam.graphql.Types
import io.github.arainko.ducktape.*
import zio.Task
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.AppointmentType.Consultation
import com.petlovefam.backend.feature.veterinary_appointment.domain.entity.AppointmentType.Vaccination
import com.petlovefam.backend.feature.pet_owner.domain.entity.PetOwner

private[route] object Mapper:

  def toGraphQL(petOwner: PetOwner): Types.PetOwner =
    petOwner
      .into[Types.PetOwner]
      .transform()

  def toGraphQL(pet: Pet, owner: Task[Types.PetOwner]): Types.Pet =
    pet
      .into[Types.Pet]
      .transform(Field.const(_.owner, owner))

  def fromGraphQL(appointmentType: Types.AppointmentType): AppointmentType = appointmentType match
    case Types.AppointmentType.VACCINATION  => AppointmentType.Vaccination
    case Types.AppointmentType.CONSULTATION => AppointmentType.Consultation

  def toGraphQL(appointmentType: AppointmentType): Types.AppointmentType = appointmentType match
    case AppointmentType.Vaccination  => Types.AppointmentType.VACCINATION
    case AppointmentType.Consultation => Types.AppointmentType.CONSULTATION

  def toGraphQL(veterinaryAppointment: VeterinaryAppointment, pet: Task[Types.Pet]): Types.VeterinaryAppointment =
    veterinaryAppointment
      .into[Types.VeterinaryAppointment]
      .transform(
        Field.computed(
          _.`type`,
          veterinaryAppointment =>
            veterinaryAppointment.`type` match
              case Consultation =>
                Types.AppointmentType.CONSULTATION
              case Vaccination =>
                Types.AppointmentType.VACCINATION
        ),
        Field.const(_.pet, pet)
      )
