package net.iessochoa.vanesa.petcare.ui.viewModel.publish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.iessochoa.vanesa.petcare.data.local.UserSessionRepository
import net.iessochoa.vanesa.petcare.data.local.provider.UserIdProvider
import net.iessochoa.vanesa.petcare.ui.viewModel.user.PublishViewModel

//Factory necesario porque PublishViewModel tiene parámetros en el constructor
//Compose no puede crearlo automáticamente, así que usamos este factory para
//pasarle manualmente el UserSessionRepository (saber q user lo va a publicar, el id) al ViewModel
class PublishViewModelFactory(
    private val sessionRepository: UserSessionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = UserIdProvider(sessionRepository)
        return PublishViewModel(sessionRepository, provider) as T
    }
}

