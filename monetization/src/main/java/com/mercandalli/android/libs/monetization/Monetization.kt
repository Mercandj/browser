package com.mercandalli.android.libs.monetization

data class Monetization private constructor(
        val inAppSku: String,
        val subscriptionSku: String
) {

    companion object {

        fun create(
                inAppSku: String,
                subscriptionSku: String
        ): Monetization {
            return Monetization(
                    inAppSku,
                    subscriptionSku
            )
        }

        fun toJson(monetization: Monetization): String {
            return "${monetization.inAppSku}#${monetization.subscriptionSku}"
        }

        fun fromJson(json: String): Monetization {
            val split = json.split("#")
            val inAppSku = split[0]
            val subscriptionSku = split[1]
            return create(
                    inAppSku,
                    subscriptionSku
            )
        }
    }
}