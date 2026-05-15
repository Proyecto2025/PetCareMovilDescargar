package net.iessochoa.vanesa.petcare.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore by preferencesDataStore("user_prefs")
