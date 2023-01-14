package me.phoenixra.atum.core.utils

fun String.formatAtum(): String {
    return StringUtils.format(this)
}

fun List<String>.formatAtum(): List<String> {
    return StringUtils.format(this)
}