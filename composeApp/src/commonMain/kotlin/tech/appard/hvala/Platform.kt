package tech.appard.hvala

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
