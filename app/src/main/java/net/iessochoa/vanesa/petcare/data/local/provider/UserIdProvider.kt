package net.iessochoa.vanesa.petcare.data.local.provider

import kotlinx.coroutines.flow.firstOrNull
import net.iessochoa.vanesa.petcare.data.local.UserSessionRepository

//Se usa para no repetir código en el userVM y en el publishVM
class UserIdProvider(private val sessionRepository: UserSessionRepository) {

    suspend fun getUserId(): Long? {
        val storedId = sessionRepository.userId.firstOrNull()
        val tempId = sessionRepository.tempUserIdFlow.firstOrNull()

        return when {
            storedId != null && storedId != -1L -> storedId
            tempId != null && tempId != -1L -> tempId
            else -> null
        }
    }
}





