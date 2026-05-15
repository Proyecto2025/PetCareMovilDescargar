package net.iessochoa.vanesa.petcare.data.local

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey

//Mantener la sesión iniciada
object UserPreferencesKeys {
    val USER_ID = longPreferencesKey("user_id")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
}
