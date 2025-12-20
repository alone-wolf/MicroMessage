package top.writerpass.micromessage.core.data.service.user.table

import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.core.data.enums.Gender
import top.writerpass.micromessage.core.data.enums.Language

object UserProfileTable : LongIdTable() {
    val userId = long("user_id")
        .references(UserTable.id)
        .uniqueIndex()
    val nickname = varchar(
        name = "nickname",
        length = 100
    )
    val avatarUrl = varchar(
        name = "avatar_url",
        length = 100
    ).nullable()
    val gender = enumeration<Gender>("gender")
    val bio = varchar(
        name = "bio",
        length = 100
    ).nullable()
    val regionCountry = varchar(
        name = "region_country",
        length = 100
    ).nullable()
    val regionProvince = varchar(
        name = "region_province",
        length = 100
    ).nullable()
    val regionCity = varchar(
        name = "region_city",
        length = 100
    ).nullable()
    val language = enumeration<Language>("language")
    val email = varchar(
        name = "email",
        length = 100
    ).nullable()
    val phone = varchar(
        name = "phone",
        length = 100
    ).nullable()
}