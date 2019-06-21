package frontend

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.content.defaultResource
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT) // Pretty Prints the JSON
        }
    }
    routing {
        static("/") {
            defaultResource("public/index.html")
        }

        static("/results") {
            defaultResource("public/results.html")
        }
    }
}

fun main() {
    embeddedServer(Netty, 8080, watchPaths = listOf("NotesAppKt"), module = Application::module).start()
}
