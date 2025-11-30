package top.writerpass.micromessage.core.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class CredentialType {
    Password, // passwordHash, salt
}