package net.iessochoa.vanesa.petcare.data.local.provider

import android.content.Context
import net.iessochoa.vanesa.petcare.data.local.UserSessionRepository

//No se cree su propio tempUserId (en cada pantalla)
object UserSessionRepositoryProvider {
    @Volatile
    private var instance: UserSessionRepository? = null

    fun get(context: Context): UserSessionRepository {
        return instance ?: synchronized(this) {
            instance ?: UserSessionRepository(context.applicationContext).also { instance = it }
        }
    }
}