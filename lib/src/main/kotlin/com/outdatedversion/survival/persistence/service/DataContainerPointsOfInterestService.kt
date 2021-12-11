package com.outdatedversion.survival.persistence.service

import com.outdatedversion.survival.Plugin
import com.outdatedversion.survival.model.PointOfInterest
import com.outdatedversion.survival.persistence.datacontainer.PointOfInterestArrayPersistentDataType
import java.util.*
import java.util.function.Consumer

class DataContainerPointsOfInterestService(private val plugin: Plugin): PointsOfInterestService {
    private val key = plugin.createKey("points_of_interest")

    override fun save(owner: UUID, poi: PointOfInterest): PointOfInterest {
        this.modifyPoints(owner) { points ->
            val existing = points.find { it.id == poi.id }
            if (existing != null) {
                points.remove(existing)
            }
            points.add(poi)
        }

        return poi
    }

    override fun get(owner: UUID, id: UUID): PointOfInterest? {
        val points = this.getAll(owner)
        return points.find { poi -> poi.id == id }
    }

    override fun getAll(owner: UUID): Array<PointOfInterest> {
        val player = this.plugin.server.getPlayer(owner) ?: throw IllegalStateException("player ($owner) must be online to set point of interest")
        val data = player.persistentDataContainer

        return data.getOrDefault(
            this.key,
            PointOfInterestArrayPersistentDataType(this.plugin),
            emptyArray()
        )
    }

    override fun remove(owner: UUID, poiId: UUID): PointOfInterest? {
        var poi: PointOfInterest? = null
        this.modifyPoints(owner) { points ->
            poi = points.find { poi -> poi.id == poiId }
            if (poi != null) {
                points.remove(poi)
            }
        }
        return poi
    }

    private fun modifyPoints(owner: UUID, handlerFn: Consumer<MutableList<PointOfInterest>>) {
        val player = this.plugin.server.getPlayer(owner) ?: throw IllegalStateException("player ($owner) must be online to set point of interest")
        val data = player.persistentDataContainer

        val points = data.getOrDefault(
            this.key,
            PointOfInterestArrayPersistentDataType(this.plugin),
            emptyArray()
        )
        val mutablePoints = points.toMutableList()
        handlerFn.accept(mutablePoints)
        player.persistentDataContainer.set(
            this.key,
            PointOfInterestArrayPersistentDataType(this.plugin),
            mutablePoints.toTypedArray()
        )
    }

}