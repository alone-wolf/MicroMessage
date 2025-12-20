package top.writerpass.micromessage.core.data.service.user.data

import kotlinx.serialization.Serializable
import top.writerpass.micromessage.core.data.base.BaseDataClass
import top.writerpass.micromessage.core.data.enums.Gender
import top.writerpass.micromessage.core.data.enums.Language

@Serializable
data class UserProfile(
    val userId: Long,
    val nickname: String,
    val avatarUrl: String?,
    val gender: Gender = Gender.Secret,
    val bio: String?,
    val regionCountry: String?,
    val regionProvince: String?,
    val regionCity: String?,
    val language: Language,
    val email: String?,
    val phone: String?,
) : BaseDataClass {
    override val id: Long
        get() = userId
}

