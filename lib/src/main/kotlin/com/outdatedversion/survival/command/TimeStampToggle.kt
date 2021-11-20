package com.outdatedversion.survival.command

import com.outdatedversion.survival.Plugin
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.time.ZoneId


class TimeStampToggle(main: Plugin) : CommandExecutor {
    private val main: Plugin
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        val namespacedKey = NamespacedKey(main, "timezone")
        val data = player.persistentDataContainer
        if (!data.has(namespacedKey, PersistentDataType.STRING)) data.set(
            namespacedKey,
            PersistentDataType.STRING,
            "+0"
        )
        val timezone = data.get(namespacedKey, PersistentDataType.STRING)
        return if (args.size == 0) {
            if (sender is Player) {
                player.sendMessage(ChatColor.GRAY.toString() + "Your current timezone is set to " + ChatColor.YELLOW.toString() + timezone)
                //player.sendMessage(ChatColor.YELLOW.toString() + "How to set your chat timezone?")
                //player.sendMessage(ChatColor.GRAY.toString() + "To set your chats timezone follow the instructions below")
                player.sendMessage(ChatColor.RED.toString() + "USAGE: " + ChatColor.GRAY.toString() + "/timezone <your-timezone>")
                //player.sendMessage(ChatColor.GREEN.toString() + "eg. /timezone America/Los_Angeles (this will set your timezone to (CA Timezone)")
                //val pastebin = TextComponent("this link.")
                //val gmtMessage = TextComponent("If you dont know your timezone click ")
//                pastebin.setUnderlined(true)
//                pastebin.color = ChatColor.YELLOW
//                pastebin.setBold(true)
//                pastebin.setItalic(true)
//                pastebin.clickEvent =
//                    ClickEvent(ClickEvent.Action.OPEN_URL, "https://pastebin.com/X1UqrpZ0")
//                player.sendMessage(gmtMessage, pastebin)
                // player.spigot().se(ChatColor.RED + "If you dont know what your GMT offset is take a look at " + Component.text(pastebin));
            } else {
                Bukkit.getLogger().info("You must be a player to run this command")
            }
            true
        } else {
            if (!args[0].isEmpty()) {
                try {
                    ZoneId.of(args[0])
                }catch(e: Exception) {
                    player.sendMessage(ChatColor.RED.toString() + "Thats not a valid timestamp!")
                    return true;
                }
                data.set(namespacedKey, PersistentDataType.STRING, args[0])
                player.sendMessage(ChatColor.YELLOW.toString() + player.name + ChatColor.GRAY + " your timezone has been set to " + args[0])
            }
//            else if (args[0].equals("show", ignoreCase = true)) {
//                player.sendMessage(ChatColor.YELLOW.toString() + player.name + ChatColor.GRAY + " your time zone is set to " + timezone)
//            }
            true
        }
    }

    init {
        this.main = main
    }
}