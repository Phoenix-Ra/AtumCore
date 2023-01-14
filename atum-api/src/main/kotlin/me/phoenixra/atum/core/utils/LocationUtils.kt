package me.phoenixra.atum.core.utils

import org.bukkit.Location

fun String.asBukkitLocation(): Location{
    return LocationUtils.getLocationFromString(this)
}
fun Location.asString(withCamera: Boolean): String{
    return LocationUtils.parseLocationToString(this, withCamera)
}