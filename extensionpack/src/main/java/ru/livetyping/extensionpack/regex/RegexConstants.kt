package ru.livetyping.extensionpack.regex

const val NAME_REGEX = "^[a-zA-Z\\s]+"

const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"

const val REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6，5])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$"

const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"

const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

const val REGEX_URL = "[a-zA-z]+://[^\\s]*"

const val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$"

const val REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"

const val REGEX_BLANK_LINE = "\\n\\s*\\r"

const val REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$"

const val REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$"

const val REGEX_INTEGER = "^-?[1-9]\\d*$"

const val REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"

const val REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$"