package me.phoenixra.atum.core.utils

fun String.formatAtum(): String {
    return StringUtils.format(this)
}

fun Collection<String>.formatAtum(): Collection<String> {
    return StringUtils.format(this)
}