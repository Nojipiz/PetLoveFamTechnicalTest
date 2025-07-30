package com.petlovefam.backend.graphql

import caliban.wrappers.Wrappers.*
import caliban.schema.ArgBuilder.auto.*
import caliban.schema.Schema.auto.*
import com.petlovefam.backend.di.DependencyInjection
import caliban.*
import com.petlovefam.backend.feature.pet_owner.application.PetOwnerService
import com.petlovefam.graphql.Operations.*
import com.petlovefam.graphql.Types
import zio.http.*
import zio.{ZIO, ZLayer}
import com.petlovefam.backend.feature.pet_owner.infrastructure.route.PetOwnerRoutes
import com.petlovefam.backend.feature.pet.infrastructure.route.PetRoutes
import com.petlovefam.backend.feature.veterinary_appointment.infrastructure.route.VeterinaryAppointmentRoutes
import caliban.CalibanError.*
import caliban.Value.StringValue
import caliban.ResponseValue
import caliban.ResponseValue.ObjectValue
import com.petlovefam.backend.graphql.GraphQLAPI.withErrorCodeExtensions

class GraphQLAPI private (
    petOwnerRoutes: PetOwnerRoutes,
    petRoutes: PetRoutes,
    veterinaryAppointmentRoutes: VeterinaryAppointmentRoutes
):

  private val queries = Query(
    petOwners = petOwnerRoutes.getPetOwners(),
    pets = petRoutes.getPets,
    veterinaryAppointments = veterinaryAppointmentRoutes.getVeterinaryAppointments
  )

  private val mutations = Mutation(
    createPetOwner = petOwnerRoutes.createPetOwner,
    createPet = petRoutes.createPet,
    createVeterinaryAppointment = veterinaryAppointmentRoutes.createVeterinaryAppointment
  )

  val api: GraphQL[Any] =
    graphQL(RootResolver(queryResolver = queries, mutationResolver = mutations)) @@ printErrors

  def run(): zio.Task[Unit] = for
    interpreter <- api.interpreter.map(withErrorCodeExtensions).map(QuickAdapter(_).handlers)
    graphiQLRequestHandler = GraphiQLHandler.handler(
      apiPath = "/api/graphql",
      wsPath = None
    )
    app = Routes(
      Method.ANY / "api" / "graphql" -> interpreter.api,
      Method.GET / "graphiql" -> graphiQLRequestHandler,
      Method.POST / "upload" / "graphql" -> interpreter.upload
    )
    _ <- app.serve[Any].provide(Server.defaultWithPort(8080))
  yield ()

object GraphQLAPI:
  val live: ZLayer[DependencyInjection.Routes, Nothing, GraphQLAPI] = ZLayer.fromFunction(new GraphQLAPI(_, _, _))

  def withErrorCodeExtensions[R](
      interpreter: GraphQLInterpreter[R, CalibanError]
  ): GraphQLInterpreter[R, CalibanError] = interpreter.mapError {
    case err @ ExecutionError(_, _, _, Some(throwable: Throwable), _) =>
      err.copy(
        msg = throwable.getMessage(),
        extensions = Some(ObjectValue(List(("errorCode", StringValue(throwable.getMessage())))))
      )
    case err: ExecutionError =>
      err.copy(extensions = Some(ObjectValue(List(("errorCode", StringValue("EXECUTION_ERROR"))))))
    case err: ValidationError =>
      err.copy(extensions = Some(ObjectValue(List(("errorCode", StringValue("VALIDATION_ERROR"))))))
    case err: ParsingError =>
      err.copy(extensions = Some(ObjectValue(List(("errorCode", StringValue("PARSING_ERROR"))))))
  }
