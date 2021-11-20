package com.outdatedversion.survival.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class TimeStampCommandCompletion : TabCompleter {
    var zoneNames: MutableList<String> = ArrayList()
    var ids = TimeZone.getAvailableIDs()
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        if (zoneNames.isEmpty()) {
            for (i in ids.indices) {
                zoneNames.add(ids[i].toString())
            }
        }
        val result: MutableList<String> = ArrayList()
        if (args.size == 2) {
            for (a in zoneNames) {
                if (a.toLowerCase().startsWith(args[1].toLowerCase())) result.add(a)
            }
            return result
        }
        return null
    }
}
