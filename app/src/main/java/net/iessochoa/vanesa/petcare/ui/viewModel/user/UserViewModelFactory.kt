package net.iessochoa.vanesa.petcare.ui.viewModel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.iessochoa.vanesa.petcare.data.local.UserSessionRepository

class UserViewModelFactory(
    private val sessionRepository: UserSessionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(sessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
