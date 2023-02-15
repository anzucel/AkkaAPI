import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import spray.json.DefaultJsonProtocol.*
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.*

final case class User(id: Long, name: String, email: String)

@main def httpserver: Unit = 
    implicit val actorSystem = ActorSystem(Behaviors.empty, "akka-http")
    implicit val userMarshaller: spray.json.RootJsonFormat[User] = jsonFormat3(User.apply)

    // val route = get {
    //     path("hello") {
    //         complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello http World!"))
    //     }
    // }

    val getUser = get {
        path("user" / LongNumber) {
            userId => complete(User(userId, "Testuser", "test@correo.com"))
        }
    }

    val createUser = post {
        path("user") {
            entity(as[User]) {
                user => complete(user)
            }
        }
    }

    val updateUser = put {
        path("user/edit") {
            entity(as[User]) {
                user => complete(user)
            }
        }
    }

    val deleteUser = delete {
        path("user" / LongNumber) {
            userId => complete(User(userId, "DeleteUser", "user@email.com"))
        }
    }

    val routes = cors() {
        concat(getUser, createUser, updateUser, deleteUser)
    }

    Http().newServerAt("127.0.0.1", 8080).bind(routes)