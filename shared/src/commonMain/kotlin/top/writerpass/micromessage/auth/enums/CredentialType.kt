package top.writerpass.micromessage.auth.enums

import kotlinx.serialization.Serializable

@Serializable
enum class CredentialType {
    Password, // passwordHash, salt
}