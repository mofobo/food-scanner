package ch.mofobo.foodscanner.common

class StringUtils {

    companion object {
        fun trimTrailingZeroAndAddSuffix(double: Double?, suffix: String, defaultValue: String?): String? {
            if (double == null) return defaultValue

            val doubleStr = double.toString()

            val result = when {
                doubleStr.isNullOrBlank() -> defaultValue
                doubleStr.indexOf(".") < 0 -> doubleStr + suffix
                else -> doubleStr.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "") + suffix
            }
            return result
        }
    }
}