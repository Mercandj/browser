package com.mercandalli.android.libs.monetization

data class Monetization private constructor(
        val subscriptionSku: String
) {

    companion object {

        fun create(
                subscriptionSku: String
        ): Monetization {
            return Monetization(
                    subscriptionSku
            )
        }

        fun toJson(monetization: Monetization): String {
            return "${monetization.subscriptionSku}"
        }

        fun fromJson(json: String): Monetization {
            return create(
                    json
            )
        }
    }
}