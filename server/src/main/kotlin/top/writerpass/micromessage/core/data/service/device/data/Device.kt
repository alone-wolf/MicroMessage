package top.writerpass.micromessage.core.data.service.device.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import top.writerpass.micromessage.auth.request.DevicePlatform
import top.writerpass.micromessage.auth.request.DeviceType
import top.writerpass.micromessage.core.data.base.BaseDataClass


@Serializable
data class Device(
    override val id: Long, // PK
    val serial: String,
    val name: String,
    val type: DeviceType,
    val platform: DevicePlatform,
    val createdAt: Long,
    val updatedAt: Long
) : BaseDataClass

object DeviceTable : LongIdTable() {
    val serial = varchar("serial", 100)
    val name = varchar("name", 100)
    val type = enumeration<DeviceType>("type")
    val platform = enumeration<DevicePlatform>("platform")
    val createdAt = long(name = "created_at")
    val updatedAt = long(name = "updated_at")
}

class DeviceEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DeviceEntity>(DeviceTable)

    var serial by DeviceTable.serial
    var name by DeviceTable.name
    var type by DeviceTable.type
    var platform by DeviceTable.platform
    var createdAt by DeviceTable.createdAt
    var updatedAt by DeviceTable.updatedAt

    fun toData(): Device {
        return Device(
            id = id.value,
            serial = serial,
            name = name,
            type = type,
            platform = platform,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
