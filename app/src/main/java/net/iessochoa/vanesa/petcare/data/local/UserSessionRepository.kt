package net.iessochoa.vanesa.petcare.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.*

//Local Storage y obtener el id para perfil y etc
class UserSessionRepository(private val context: Context) {

    private val TAG = "UserSessionRepository"

    //ID temporal en memoria (si no marca mantener sesión)
    private var tempUserId: Long? = null

    //Exponer el tempUserId
    private val _tempUserIdFlow = MutableStateFlow<Long?>(null)
    val tempUserIdFlow: StateFlow<Long?> = _tempUserIdFlow


    fun setTempUserId(id: Long) {
        tempUserId = id
        _tempUserIdFlow.value = id
    }


    val isLoggedIn: Flow<Boolean> =
        context.userDataStore.data.map { prefs ->
            val value = prefs[UserPreferencesKeys.IS_LOGGED_IN] ?: false
            Log.d(TAG, "isLoggedIn FLOW EMITS: $value")
            value
        }

    val userId: Flow<Long> =
        context.userDataStore.data.map { prefs ->
            val value = prefs[UserPreferencesKeys.USER_ID] ?: -1L
            Log.d(TAG, "userId FLOW EMITS: $value")
            value
        }

    suspend fun saveSession(id: Long) {
        Log.d(TAG, "saveSession($id) CALLED")
        context.userDataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = id
            prefs[UserPreferencesKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        Log.d(TAG, "clearSession() CALLED")
        context.userDataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = -1L
            prefs[UserPreferencesKeys.IS_LOGGED_IN] = false
        }

        tempUserId = null
        _tempUserIdFlow.value = null
    }

    suspend fun getSessionId(): Long? {
        val id = context.userDataStore.data.map { prefs ->
            prefs[UserPreferencesKeys.USER_ID] ?: -1L
        }.firstOrNull()

        Log.d(TAG, "getSessionId() RETURNS: $id")

        return id.takeIf { it != -1L }
    }

    suspend fun setLoggedIn() {
        Log.d(TAG, "setLoggedIn() CALLED")
        context.userDataStore.edit { prefs ->
            prefs[UserPreferencesKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun saveUserId(id: Long) {
        Log.d(TAG, "saveUserId($id) CALLED")
        context.userDataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = id
        }
    }
}
