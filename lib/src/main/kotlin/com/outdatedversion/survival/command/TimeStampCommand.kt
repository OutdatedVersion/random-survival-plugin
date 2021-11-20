package com.outdatedversion.survival.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import com.outdatedversion.survival.Plugin
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.ZoneId
import java.util.*
// Now using Annotation Command Framework (ACF)
@CommandAlias("timezone|tz")
class TimeStampCommand(main: Plugin): BaseCommand() {
  private val main: Plugin

    @Default
    @CommandCompletion("@timeZoneIDS") // Gets list from Plugin.kt (Main)
  fun handleCommand(player: Player, vararg message: String) {
      //val player = player as Player
      val namespacedKey = NamespacedKey(main, "timezone") // Gets value of key(timezone) from Player Persistent Data
      val data = player.persistentDataContainer // Gets the Persistent Data for player
        // If timezone data does not exist Create it and set value to +0 *Idk if it does anything with the current framework but better safe than sorry*
      if (!data.has(namespacedKey, PersistentDataType.STRING)) data.set(
          namespacedKey,
          PersistentDataType.STRING,
          "+0"
      )
      val timezone = data.get(namespacedKey, PersistentDataType.STRING) // Gets TimeZone ZoneID for player
        // Check args(message) to see if input command contains args
      if (message.size == 0) {
          player.sendMessage(ChatColor.GRAY.toString() + "Your current timezone is set to " + ChatColor.YELLOW.toString() + timezone)
          player.sendMessage(ChatColor.RED.toString() + "USAGE: " + ChatColor.GRAY.toString() + "/timezone <your-timezone>")
          true
          // If args are not empty:
      }else if (!message[0].isEmpty()) {
              try {
                  ZoneId.of(message[0])
                  data.set(namespacedKey, PersistentDataType.STRING, message[0])
                  player.sendMessage(ChatColor.YELLOW.toString() + player.name + ChatColor.GRAY + " your timezone has been set to " + message[0])
              } catch (e: Exception) {
                  player.sendMessage(ChatColor.RED.toString() + "Thats not a valid timestamp!")
              }

          }
          true
      }
    // Initializes Main Class (Plugin)
    init {
        this.main = main
    }
  }
