# Petlovefam Backend

A GraphQL API backend for pet management built with Scala 3, ZIO, and Caliban, following Clean Architecture principles with vertical slice organization.


## 🚀 Quick Start with Docker

**Run this command**
```bash
docker build -t petlovefam-backend . && docker run -p 8080:8080 petlovefam-backend
```
Access the GraphiQL interface at: http://localhost:8080/graphiql

## 🏗️ Architecture

This project implements **Clean Architecture** with **Vertical Slices** pattern:

### Clean Architecture Layers
- **Domain Layer**: Business entities, repositories (interfaces), and domain errors
- **Application Layer**: Use cases and business logic coordination
- **Infrastructure Layer**: Database implementations, external services, and GraphQL routes

### Vertical Slices
Each business feature is organized as a complete vertical slice:
```
feature/
├── domain/
│   ├── entity/           # Business entities
│   ├── repository/       # Repository interfaces
│   └── error/           # Domain-specific errors
├── application/         # Use cases and services
└── infrastructure/
    ├── repository/      # Database implementations
    └── route/           # GraphQL schema definitions
```

## 🏢 Business Domains

### Pet Owner Domain
- **Entity**: `PetOwner(id, name, email)`
- **Operations**: Create pet owners, list all pet owners
- **Validation**: Email uniqueness (planned)

### Pet Domain
- **Entity**: `Pet(id, petOwnerId, name, breed, birthDate, pictureUrl)`
- **Operations**: Create pets, list all pets
- **Validation**: Pet owner must exist before creating a pet
- **Domain Error**: `PetNotFound`, `PetOwnerNotFound`

### Veterinary Appointment Domain
- **Entity**: `VeterinaryAppointment(id, petId, date, type)`
- **Types**: `CHECKUP`, `VACCINATION`, `SURGERY`, `EMERGENCY`
- **Operations**: Create appointments, list all appointments
- **Validation**: Pet must exist before creating an appointment
- **Domain Error**: `PetNotFound`

## 🔄 Service Dependencies

Services follow the **Repository Pattern** and depend only on repositories, not other services:

```
VeterinaryAppointmentService → VeterinaryAppointmentRepository + PetRepository
PetService → PetRepository + PetOwnerRepository
PetOwnerService → PetOwnerRepository
```

This ensures:
- ✅ No service-to-service coupling
- ✅ Clean separation of concerns
- ✅ Easy testing with mocked repositories
- ✅ Simple dependency injection

## 🗄️ Database

- **Type**: SQLite (embedded)
- **ORM**: Quill with ZIO integration
- **Migration**: Automatic table creation on startup
- **Location**: Local file system

### Schema
```sql
-- Pet Owners
CREATE TABLE PetOwner (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL
);

-- Pets
CREATE TABLE Pet (
    id TEXT PRIMARY KEY,
    petOwnerId TEXT NOT NULL,
    name TEXT NOT NULL,
    breed TEXT NOT NULL,
    birthDate TEXT NOT NULL,
    pictureUrl TEXT,
    FOREIGN KEY (petOwnerId) REFERENCES PetOwner(id)
);

-- Veterinary Appointments
CREATE TABLE VeterinaryAppointment (
    id TEXT PRIMARY KEY,
    petId TEXT NOT NULL,
    date TEXT NOT NULL,
    type TEXT NOT NULL,
    FOREIGN KEY (petId) REFERENCES Pet(id)
);
```

## 🚀 GraphQL API

### Endpoints
- **GraphQL**: `POST /graphql`
- **GraphiQL**: `GET /graphiql` (development interface)

### Available Operations

#### Pet Owners
```graphql
# Create a pet owner
mutation {
  createPetOwner(name: "John Doe", email: "john@example.com") {
    id
    name
    email
  }
}

# List all pet owners
query {
  petOwners {
    id
    name
    email
  }
}
```

#### Pets
```graphql
# Create a pet
mutation {
  createPet(input: {
    petOwnerId: "owner-uuid"
    name: "Buddy"
    breed: "Golden Retriever"
    birthDate: "2020-01-15"
    pictureUrl: "https://example.com/buddy.jpg"
  }) {
    id
    name
    breed
    birthDate
  }
}

# List all pets
query {
  pets {
    id
    name
    breed
    petOwnerId
  }
}
```

#### Veterinary Appointments
```graphql
# Create an appointment
mutation {
  createAppointment(
    petId: "pet-uuid"
    date: "2024-12-15T10:00:00Z"
    type: CONSULTATION
  ) {
    id
    petId
    date
    type
  }
}

# List all appointments
query {
  appointments {
    id
    petId
    date
    type
  }
}
```

## 🛡️ Validation & Error Handling

The system implements comprehensive validation:

### Domain-Level Validation
- **Pet Creation**: Validates pet owner exists
- **Appointment Creation**: Validates pet exists
- **Type-Safe Errors**: Sum types for domain-specific errors

### Error Types
```scala
// Pet domain errors
PetDomainError.PetNotFound(petId)

// Pet owner domain errors
PetOwnerDomainError.PetOwnerNotFound(petOwnerId)
```

### Error Responses
GraphQL errors include descriptive messages:
```json
{
  "errors": [
    {
      "message": "Pet with id 'invalid-id' not found"
    }
  ]
}
```


## 🛠️ Technology Stack

- **Language**: Scala 3
- **Effect System**: ZIO 2.x
- **GraphQL**: Caliban
- **Database**: SQLite + Quill
- **Architecture**: Clean Architecture + Vertical Slices
- **Dependency Injection**: ZLayer
- **Build Tool**: SBT
