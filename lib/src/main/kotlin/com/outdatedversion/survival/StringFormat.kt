package com.outdatedversion.survival

fun snakecaseToDisplay(name: String): String {
    return name.lowercase()
        .replace("_", " ")
        .replace(Regex("(^| )[a-z]")) { it.value.uppercase() }
}