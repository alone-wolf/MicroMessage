package top.writerpass.micromessage.core.data.service.user.table

import org.jetbrains.exposed.dao.id.LongIdTable

object UserTable : LongIdTable(){
    val createdAt = long("created_at")
}

