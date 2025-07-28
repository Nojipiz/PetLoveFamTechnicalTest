package com.petlovefam.backend.config

import com.petlovefam.backend.config.Config.DBConfig

case class Config(
    dbConfig: DBConfig
)

object Config:
  case class DBConfig(
      driver: String,
      url: String,
      user: String,
      password: String,
      migrationsLocation: String,
      threads: Int
  )
