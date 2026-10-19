package com.sanvalero.glowshop.domain.usecase

class NormalizeDominicanPhone {
    /** Returns the international WhatsApp phone format, or null when invalid. */
    operator fun invoke(input: String): String? {
        val digits = input.filter(Char::isDigit)
        val local = when {
            digits.length == 10 && digits.startsWith("1") -> digits
            digits.length == 10 -> "1$digits"
            digits.length == 11 && digits.startsWith("1") -> digits
            else -> return null
        }
        val areaCode = local.substring(1, 4)
        if (areaCode !in setOf("809", "829", "849")) return null
        return local
    }
}
