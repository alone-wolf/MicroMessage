package top.writerpass.micromessage.auth.enums

import kotlinx.serialization.Serializable


@Serializable
enum class LoginType {
    UsernamePassword,
    PhoneNumberPassword,
    EmailPassword,
    PhoneNumberCode,
    EmailCode
}