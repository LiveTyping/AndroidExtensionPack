package ru.livetyping.extensionpack.regex

fun CharSequence.isMatch(regex: String) = Regex(regex).matches(this)