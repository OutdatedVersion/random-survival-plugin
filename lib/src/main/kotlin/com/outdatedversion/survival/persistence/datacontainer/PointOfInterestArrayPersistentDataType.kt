package com.outdatedversion.survival.persistence.datacontainer;

import com.outdatedversion.survival.Plugin
import com.outdatedversion.survival.model.PointOfInterest
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import java.util.*

class PointOfInterestArrayPersistentDataType(private val plugin: Plugin): PersistentDataType<Array<PersistentDataContainer>, Array<PointOfInterest>> {
    override fun getPrimitiveType(): Class<Array<PersistentDataContainer>> {
        return Array<PersistentDataContainer>::class.java
    }

    override fun getComplexType(): Class<Array<PointOfInterest>> {
        return Array<PointOfInterest>::class.java
    }

    override fun toPrimitive(complex: Array<PointOfInterest>, context: PersistentDataAdapterContext): Array<PersistentDataContainer> {
        return complex.map { poi ->
            val container = context.newPersistentDataContainer()
            container.set(plugin.createKey("id"), UuidPersistentDataType.Default, poi.id)
            container.set(plugin.createKey("block_key"), VectorPersistentDataType.Default, poi.coords)
            container.set(plugin.createKey("world_environment_name"), PersistentDataType.STRING, poi.env.name)
            container.set(plugin.createKey("context"), PersistentDataType.STRING, poi.context)
            return@map container
        }.toTypedArray()
    }

    override fun fromPrimitive(primitive: Array<PersistentDataContainer>, adapterContext: PersistentDataAdapterContext): Array<PointOfInterest> {
        return primitive.map { data ->
            // default for v1 migration
            val id = data.getOrDefault(plugin.createKey("id"), UuidPersistentDataType.Default, UUID.randomUUID())
            var coords = data.get(plugin.createKey("block_key"), VectorPersistentDataType.Default)
            // v1 migration
            if (coords == null) {
                val x = data.get(plugin.createKey("x"), PersistentDataType.DOUBLE)!!
                val y = data.get(plugin.createKey("y"), PersistentDataType.DOUBLE)!!
                val z = data.get(plugin.createKey("z"), PersistentDataType.DOUBLE)!!
                coords = Vector(x, y, z)
            }
            val envName = data.get(plugin.createKey("world_environment_name"), PersistentDataType.STRING)!!
            val ctx = data.get(plugin.createKey("context"), PersistentDataType.STRING)!!

            return@map PointOfInterest(
                id=id,
                coords=coords,
                env=World.Environment.valueOf(envName),
                context=ctx,
            )
        }.toTypedArray()
    }
}
