-- Initial schema for PetLoveFam backend
-- Creates all required tables for SQLite

-- Create pet_owner table
CREATE TABLE pet_owner (
    id TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE
);

-- Create pet table
CREATE TABLE pet (
    id TEXT PRIMARY KEY NOT NULL,
    pet_owner_id TEXT NOT NULL,
    name TEXT NOT NULL,
    breed TEXT NOT NULL,
    birth_date TEXT NOT NULL,
    picture_url TEXT,
    FOREIGN KEY (pet_owner_id) REFERENCES pet_owner(id) ON DELETE CASCADE
);

-- Create veterinary_appointment table
CREATE TABLE veterinary_appointment (
    id TEXT PRIMARY KEY NOT NULL,
    pet_id TEXT NOT NULL,
    date TEXT NOT NULL,
    type TEXT NOT NULL CHECK (type IN ('Consultation', 'Vaccination')),
    FOREIGN KEY (pet_id) REFERENCES pet(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_pet_owner_email ON pet_owner(email);
CREATE INDEX idx_veterinary_appointment_pet_id ON veterinary_appointment(pet_id);
CREATE INDEX idx_veterinary_appointment_date ON veterinary_appointment(date);
CREATE INDEX idx_veterinary_appointment_type ON veterinary_appointment(type);
