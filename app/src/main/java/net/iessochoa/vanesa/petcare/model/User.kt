package net.iessochoa.vanesa.petcare.model

data class User (
    val nombreUsuario: String,
    val numPublicaciones: Int,
    val nombreCompleto: String,
    val numTelefono: String,
    val correo: String,
    val contrasenya: String
)