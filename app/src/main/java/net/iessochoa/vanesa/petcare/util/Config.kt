package net.iessochoa.vanesa.petcare.util

import android.content.Context
import java.util.Properties

object Config {
    fun getApiKey(context: Context): String {
        val props = Properties()
        context.assets.open("api.properties").use { input ->
            props.load(input)
        }
        return props.getProperty("API_KEY") ?: ""
    }
}
