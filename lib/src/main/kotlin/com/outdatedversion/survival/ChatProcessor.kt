package com.outdatedversion.survival

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

interface ChatProcessor {
    fun processAndSendPlayerChat(player: Player, message: Component)
}