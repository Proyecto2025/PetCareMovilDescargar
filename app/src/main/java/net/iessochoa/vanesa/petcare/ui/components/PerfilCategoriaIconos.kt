package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import net.iessochoa.vanesa.petcare.model.CategoriaPost

@Composable
fun PerfilCategoriaIconos(
    icon: ImageVector,
    categoria: CategoriaPost,
    selected: CategoriaPost?,
    onClick: (CategoriaPost?) -> Unit
) {
    val isSelected = categoria == selected
    val color = if (isSelected) Color(0xFF0C653D) else Color.Gray

    Icon(
        imageVector = icon,
        contentDescription = categoria.name,
        tint = color, //Se ponga del color
        modifier = Modifier
            .size(40.dp)
            .clickable {
                if (!isSelected) {
                    onClick(categoria) //Cambia si no está selecionada, básicamente q si le das dos clicks no se quite y ponga el color
                }
            }
    )
}