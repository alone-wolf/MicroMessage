package top.writerpass.micromessage.auth.enums

import kotlinx.serialization.Serializable

@Serializable
enum class IdentifierType {
    Username, // single, required
    Email, // multiple, optional
    Phone // multiple, optional
}