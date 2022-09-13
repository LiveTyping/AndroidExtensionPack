package ru.livetyping.extensionpack.regex

inline val CharSequence?.isMobileSimple: Boolean
    get() {
        return isMatch(REGEX_MOBILE_SIMPLE)
    }

inline val CharSequence?.isMobileExact: Boolean
    get() {
        return isMatch(REGEX_MOBILE_EXACT)
    }

inline val CharSequence?.isEmail: Boolean
    get() {
        return isMatch(REGEX_EMAIL)
    }

inline val CharSequence?.isURL: Boolean
    get() {
        return isMatch(REGEX_URL)
    }

inline val CharSequence?.isZh: Boolean
    get() {
        return isMatch(REGEX_ZH)
    }

inline val CharSequence?.isIP: Boolean
    get() {
        return isMatch(REGEX_IP)
    }

fun CharSequence?.isMatch(regex: String): Boolean {
    return !this.isNullOrEmpty() && Regex(regex).matches(this)
}