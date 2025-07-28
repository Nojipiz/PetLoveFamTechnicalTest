package com.petlovefam.backend.feature.pet.application.request

case class CreatePetRequest(
    petOwnerId: String,
    name: String,
    breed: String,
    birthDate: String,
    pictureUrl: Option[String]
)
