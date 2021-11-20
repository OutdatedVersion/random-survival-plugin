package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.outdatedversion.survival.Plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.ZoneId

@CommandAlias("timezone|tz")
class TimeZoneCommand(private val plugin: Plugin) : BaseCommand() {

    @Default
    @CommandCompletion("@timezones")
    fun handleCommand(player: Player, @Single @Optional zone: String?) {
        val timezoneDataKey = NamespacedKey(plugin, "timezone")
        val data = player.persistentDataContainer
        val timezone = data.get(timezoneDataKey, PersistentDataType.STRING)

        if (zone.isNullOrBlank()) {
            if (timezone.isNullOrBlank()) {
                player.sendMessage(
                    Component.text(
                        "Your timezone has not yet been set.",
                        NamedTextColor.GRAY,
                        TextDecoration.BOLD
                    )
                )
            } else {
                player.sendMessage(
                    Component.text(
                        "Your current timezone is set to ",
                        NamedTextColor.GRAY
                    ).append(Component.text(timezone, NamedTextColor.YELLOW))
                )
            }

            player.sendMessage(
                Component.text(
                    "USAGE: ",
                    NamedTextColor.RED
                ).append(Component.text("/timezone <your-timezone>", NamedTextColor.YELLOW))
            )
        } else {
            try {
                ZoneId.of(zone, ZoneId.SHORT_IDS)
            } catch (e: Exception) {
                player.sendMessage(Component.text("That is not a valid timezone!", NamedTextColor.RED))
                return
            }

            data.set(timezoneDataKey, PersistentDataType.STRING, zone)
            player.sendMessage(
                Component.text("Your timezone has been set to ", NamedTextColor.GRAY)
                    .append(Component.text(zone, NamedTextColor.YELLOW))
            )
        }
    }
}
