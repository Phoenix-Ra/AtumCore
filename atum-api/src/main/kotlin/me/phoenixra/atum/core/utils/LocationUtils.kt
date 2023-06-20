package me.phoenixra.atum.core.utils

import me.phoenixra.atum.core.config.Config
import org.bukkit.Location

fun String.asBukkitLocation(): Location{
    return LocationUtils.getLocationFromString(this)
}
fun Location.asString(withCamera: Boolean): String{
    return LocationUtils.parseLocationToString(this, withCamera)
}


fun Config.getLocation(path: String): Location?{
    val config = this.getSubsection(path) ?: return null
    return LocationUtils.getLocationFromConfig(config)
}