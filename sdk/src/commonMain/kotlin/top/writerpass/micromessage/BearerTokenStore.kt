package top.writerpass.micromessage

object BearerTokenStore {
    @Volatile
    var token:String? = null
}