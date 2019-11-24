package com.zlz2000

import com.fasterxml.jackson.databind.SerializationFeature
import com.google.gson.Gson
import com.zlz2000.Bean.*
import com.zlz2000.Dao.saveQuestion
import com.zlz2000.Server.Code
import com.zlz2000.Server.User
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.HttpsRedirect
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.resolveResource
import io.ktor.jackson.jackson
import io.ktor.network.tls.certificates.generateCertificate
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import kotlinx.coroutines.plus
import me.liuwj.ktorm.database.Database
import java.io.File

val db = Database.connect(
    "jdbc:mysql://localhost:3306/dtxt?serverTimezone=UTC",
    "com.mysql.cj.jdbc.Driver",
    "zlz",
    "Whyzshngrd1@db"
)

fun main(args: Array<String>): Unit {
    // generate SSL certificate
    val file = File("build/temporary.jks")
    if (!file.exists()) {
        file.parentFile.mkdirs()
        generateCertificate(file)
    }
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    // https://ktor.io/servers/features/https-redirect.html#testing
    if (!testing) {
        install(HttpsRedirect) {
            // The port to redirect to. By default 443, the default HTTPS port.
            sslPort = 443
            // 301 Moved Permanently, or 302 Found redirect.
            permanentRedirect = true
        }
    }

    install(ContentNegotiation) {
        gson {
        }

        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Sessions) {
        cookie<PhoneNumber>("PhoneNumber")
    }

    val client = HttpClient(Apache) {
    }

    routing {
        get("/") {
            call.resolveResource("index.html")?.let { it1 -> call.respond(it1) }
        }

        post("/sendCode/{type}") {
            val type = call.parameters["type"] ?: ""
            val data = Gson().fromJson(call.receiveText(), CodeBean::class.java)
            try {
                call.respond(Code().sendCode(data.type, data.number, type))
            } catch (e: Exception) {
                println("sendCode Error:$e")
            }
        }

        post("/register") {
            val data = Gson().fromJson(call.receiveText(), RegisterBean::class.java)
            try {
                call.respond(User().register(data))
            } catch (e: Exception) {
                println(e.toString())
            }
            println(data.toString())
        }

        post("/login") {
            val data = Gson().fromJson(call.receiveText(), LoginBean::class.java)
            try {
                call.respond(User().login(data))
            } catch (e: Exception) {
                println(e.toString())
            }
            println(data.toString())
        }

        post("/forget") {
            val data = Gson().fromJson(call.receiveText(), ForgetBean::class.java)
            try {
                call.respond(User().forget(data))
            } catch (e: Exception) {
                println(e.toString())
            }
            println(data.toString())
        }

        post("/question/add/choose") {
            val data = Gson().fromJson(call.receiveText(), QuestionChoose::class.java)
            println(data.toString())
            if (saveQuestion(data)) {
                call.respond(mapOf("code" to 200))
            } else {
                call.respond(mapOf("code" to 400, "mag" to "保存出错"))
            }
        }

        post("/question/add/tOrf") {
            val data = Gson().fromJson(call.receiveText(), QuestionTOrF::class.java)
            println(data.toString())
            if (saveQuestion(data)) {
                call.respond(mapOf("code" to 200))
            } else {
                call.respond(mapOf("code" to 400, "mag" to "保存出错"))
            }
        }

        post("/question/add/fill") {
            val data = Gson().fromJson(call.receiveText(), QuestionFill::class.java)
            println(data.toString())
            if (saveQuestion(data)) {
                call.respond(mapOf("code" to 200))
            } else {
                call.respond(mapOf("code" to 400, "mag" to "保存出错"))
            }
        }

        get("{html}") {
            val name = call.parameters["html"] ?: ""
            call.resolveResource(name)?.let { it1 -> call.respond(it1) }
        }

        get("{type}/{name}") {
            val type = call.parameters["type"] ?: ""
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, type)?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/{name}") {
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/")?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/{type}/{name}") {
            val type = call.parameters["type"] ?: ""
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/${type}")?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/css/modules/{name}") {
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/css/modules")?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/css/modules/laydate/default/{name}") {
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/css/modules/laydate/default")?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/css/modules/layer/default/{name}") {
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/css/modules/layer/default")?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/images/face/{name}") {
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/images/face")?.let { it1 -> call.respond(it1) }
        }

        get("lib/layui/lay/modules/{name}") {
            val name = call.parameters["name"] ?: ""
            call.resolveResource(name, "lib/layui/lay/modules")?.let { it1 -> call.respond(it1) }
        }
    }
}

data class PhoneNumber(
    val phoneNumber: String
)
