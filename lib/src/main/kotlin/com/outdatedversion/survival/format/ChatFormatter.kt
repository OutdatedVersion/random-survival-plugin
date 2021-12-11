package com.outdatedversion.survival.format

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.function.BiFunction

interface ChatFormatter: BiFunction<Player, Component, Component>