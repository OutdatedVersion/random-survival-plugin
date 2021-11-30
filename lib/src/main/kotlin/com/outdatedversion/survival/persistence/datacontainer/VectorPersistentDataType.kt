package com.outdatedversion.survival.persistence.datacontainer

import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector

class VectorPersistentDataType: PersistentDataType<Long, Vector> {
    companion object {
        val Default: VectorPersistentDataType
            get() = VectorPersistentDataType()
    }

    override fun getPrimitiveType(): Class<Long> {
        return Long::class.javaObjectType
    }

    override fun getComplexType(): Class<Vector> {
        return Vector::class.java
    }

    override fun toPrimitive(vector: Vector, context: PersistentDataAdapterContext): Long {
        return Block.getBlockKey(vector.blockX, vector.blockY, vector.blockZ)
    }

    override fun fromPrimitive(primitive: Long, context: PersistentDataAdapterContext): Vector {
        return Vector(
            Block.getBlockKeyX(primitive),
            Block.getBlockKeyY(primitive),
            Block.getBlockKeyZ(primitive),
        )
    }
}