package com.petlovefam.backend.feature.pet.domain.entity

case class Pet(
    id: String,
    petOwnerId: String,
    name: String,
    breed: String,
    birthDate: String,
    pictureUrl: Option[String] = None
)
