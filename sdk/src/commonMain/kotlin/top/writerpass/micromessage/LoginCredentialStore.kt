package top.writerpass.micromessage

object LoginCredentialStore {
    @Volatile
    var username: String? = null

    @Volatile
    var passwordHash0: String? = null
}
