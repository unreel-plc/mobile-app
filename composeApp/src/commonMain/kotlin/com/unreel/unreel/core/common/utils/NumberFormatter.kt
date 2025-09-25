package com.unreel.unreel.core.common.utils
import kotlin.math.pow

object NumberFormatter {

    fun formatNumberWith2Decimals(value: Double): String {
        return formatDecimal(value, 2)
    }

    fun formatNumberWithCommas(value: Long): String {
        return addCommasToNumber(value.toString())
    }

    fun formatCurrency(value: Double, currencySymbol: String = "ETB"): String {
        return "${formatDecimal(value, 2)} $currencySymbol"
    }

    fun formatDecimal(value: Double, decimals: Int = 2): String {
        val multiplier = 10.0.pow(decimals)
        val roundedValue = kotlin.math.round(value * multiplier) / multiplier

        val integerPart = roundedValue.toLong()
        val decimalPart = ((roundedValue - integerPart) * multiplier).toLong()

        return if (decimals == 0) {
            addCommasToNumber(integerPart.toString())
        } else {
            val decimalString = decimalPart.toString().padStart(decimals, '0')
            "${addCommasToNumber(integerPart.toString())}.$decimalString"
        }
    }

    fun formatPercentage(value: Double): String {
        return "${formatDecimal(value, 1)}%"
    }

    private fun addCommasToNumber(numberString: String): String {
        val reversed = numberString.reversed()
        val chunked = reversed.chunked(3)
        return chunked.joinToString(",").reversed()
    }

    fun parseNumber(formattedNumber: String): Double? {
        return try {
            formattedNumber.replace(",", "").replace("[^\\d.-]".toRegex(), "").toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

// Extension functions for easy use
fun Double.formatWith2Decimals(): String = NumberFormatter.formatNumberWith2Decimals(this)
fun Double.formatCurrency(symbol: String = "ETB"): String = NumberFormatter.formatCurrency(this, symbol)
fun Double.formatDecimal(decimals: Int = 2): String = NumberFormatter.formatDecimal(this, decimals)
fun Double.formatPercentage(): String = NumberFormatter.formatPercentage(this)
fun Long.formatWithCommas(): String = NumberFormatter.formatNumberWithCommas(this)

// String extensions
fun String.formatAsNumber(): String {
    val cleanNumber = this.replace(",", "").toLongOrNull() ?: 0L
    return cleanNumber.formatWithCommas()
}

fun String.parseFormattedNumber(): Double? = NumberFormatter.parseNumber(this)

// Safe number operations
fun String.toSafeDouble(): Double {
    return this.replace(",", "").replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
}

fun String.toSafeLong(): Long {
    return this.replace(",", "").replace("[^\\d]".toRegex(), "").toLongOrNull() ?: 0L
}