package com.outdatedversion.survival

import com.outdatedversion.survival.persistence.service.PointsOfInterestService
import org.apache.logging.log4j.LogManager
import org.bukkit.Statistic
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

    @EventHandler
    fun updateScoreboardDeathCount(event: PlayerJoinEvent) {
        val player = event.player
        val scoreboard = player.scoreboard
        val obj = scoreboard.getObjective("Deaths") ?: return
        val playerScoreboardEntry = obj.getScore(player.name)

        val deathCount = player.getStatistic(Statistic.DEATHS)
        if (playerScoreboardEntry.score != deathCount) {
            playerScoreboardEntry.score = deathCount
            this.logger.info("Updated death count to $deathCount for ${player.uniqueId}")
        }
    }
}