package me.phoenixra.atum.core.command

import me.phoenixra.atum.core.exceptions.NotificationException
import java.util.function.Predicate


fun <T> T?.notifyNull(msg: String, asLang: Boolean): T {
    return this ?: throw NotificationException(msg,asLang)
}


fun <T> T.notifyFalse(predicate: Predicate<T>, msg: String, asLang: Boolean): T {
    predicate.test(this).notifyFalse(msg, asLang)
    return this
}

fun Boolean.notifyFalse(msg: String, asLang: Boolean): Boolean {
    return if (this) true else throw NotificationException(msg, asLang)
}