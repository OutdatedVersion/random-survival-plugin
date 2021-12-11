package com.outdatedversion.survival

import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import org.apache.logging.log4j.LogManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class DataMigrationEventListener(private val poiService: PointsOfInterestService): Listener {
    private val logger = LogManager.getLogger(DataMigrationEventListener::class.java)

    @EventHandler
    fun handleJoin(event: PlayerJoinEvent) {
        val points = this.poiService.getAll(event.player.uniqueId)
        val pointsWithoutOwner = points.filter { it.ownerId == null }

        pointsWithoutOwner.map { it.copy(ownerId = event.player.uniqueId) }
                          .forEach {
                              this.logger.info("Added missing owner ID (${it.ownerId}) to ${it.id}")
                              this.poiService.save(it.ownerId!!, it)
                          }
    }
}