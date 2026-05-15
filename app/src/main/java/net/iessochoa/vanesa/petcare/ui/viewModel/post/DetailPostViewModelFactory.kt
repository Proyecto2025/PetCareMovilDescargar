package net.iessochoa.vanesa.petcare.ui.viewModel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Hace falta esto en post pq no devuelve todo la api (en advice sí q devuelve todos los campos),
// y pq se pasa el id en DetailPostViewModel
class DetailPostViewModelFactory(
    private val postId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailPostViewModel(postId) as T
    }
}