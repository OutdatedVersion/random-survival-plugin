package com.outdatedversion.survival.persistence.datacontainer

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.*

class UuidPersistentDataType: PersistentDataType<ByteArray, UUID> {
    companion object {
        val Default: UuidPersistentDataType
            get() = UuidPersistentDataType()
    }

    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<UUID> {
        return UUID::class.java
    }

    override fun toPrimitive(complex: UUID, context: PersistentDataAdapterContext): ByteArray {
        val buffer = ByteBuffer.wrap(ByteArray(16))
        buffer.putLong(complex.mostSignificantBits)
        buffer.putLong(complex.leastSignificantBits)
        return buffer.array()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): UUID {
        val buffer = ByteBuffer.wrap(primitive)
        return UUID(buffer.long, buffer.long)
    }
}