package top.writerpass.micromessage.sdk

class ApiClient(baseUrl: String) {
    private val client = HttpClientFactory.create(baseUrl = baseUrl)

//    suspend fun requestDebugDump() {
//        val r = client.post("/debug/dump")
//        val b = r.bodyAsText()
//        print(b)
//    }

    val auth = AuthService(client)
//    val user = UserService(client)
//    val message = MessageService(client)
}