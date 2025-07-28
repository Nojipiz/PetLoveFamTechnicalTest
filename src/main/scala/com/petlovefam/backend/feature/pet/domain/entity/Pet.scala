package com.petlovefam.backend.feature.pet.domain.entity

case class Pet(
    id: String,
    name: String,
    breed: String,
    birthDate: String,
    picture: Option[String] = None
)
