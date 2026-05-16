package net.iessochoa.vanesa.petcare.data.api.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import net.iessochoa.vanesa.petcare.data.api.PetCareApiService
import net.iessochoa.vanesa.petcare.data.model.dto.request.advice.CreateAdviceDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.post.CreateAdoptionPostDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.post.CreatePostDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.CreateUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.EditPasswordDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.EditUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.LoginUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.user.CreateUserDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.LoginUserDto
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.Response
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import kotlin.math.min

object PetCareRepository {
    private const val JSON_MEDIA_TYPE = "application/json"


    private const val BASE_URL = "https://petcare-api-r9b6.onrender.com/petCare/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())
            )
            .baseUrl(BASE_URL)
            .build()
    }

    private val service: PetCareApiService by lazy {
        retrofit.create(PetCareApiService::class.java)
    }

    //Convertir de URI a Multipart
    private fun uriToMultipart(context: Context, uri: Uri): MultipartBody.Part {
        val compressedBytes = compressImage(context, uri)

        val requestFile = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "image",
            "foto_${System.currentTimeMillis()}.jpg",
            requestFile
        )
    }

    //Reducir el tamaño de las imágenes y corregir orientación para evitar HTTP 413 y fotos giradas
    private fun compressImage(context: Context, uri: Uri): ByteArray {

        //Leer bitmap original
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        //Leer EXIF para saber si la imagen está girada
        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        //Crear bitmap corregido
        val correctedBitmap = Bitmap.createBitmap(
            originalBitmap,
            0, 0,
            originalBitmap.width,
            originalBitmap.height,
            matrix,
            true
        )

        //Redimensionar a máximo 1080px (como Instagram)
        val maxSize = 1080
        val ratio = min(
            maxSize.toFloat() / correctedBitmap.width,
            maxSize.toFloat() / correctedBitmap.height
        )

        val width = (correctedBitmap.width * ratio).toInt()
        val height = (correctedBitmap.height * ratio).toInt()

        val resizedBitmap = Bitmap.createScaledBitmap(correctedBitmap, width, height, true)

        //Comprimir a JPEG 70%
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

        return outputStream.toByteArray()
    }


    //User
    suspend fun login(body: LoginUserDtoReq): LoginUserDto? {
        return try {
            service.login(body)
        } catch (e: Exception) {
            android.util.Log.e("LOGIN_ERROR", "Error en login: ${e.message}", e)
            null
        }
    }


    suspend fun createUser(body: CreateUserDtoReq): Response<CreateUserDto> {
        return service.createUser(body)
    }



    suspend fun getPerfil(id: Long) =
        service.getPerfil(id)

    suspend fun getPerfilFiltrado(id: Long, type: String) =
        service.getPerfilFiltrado(id, type)



    suspend fun editUser(id: Long, body: EditUserDtoReq) {
        val response = service.editUser(id, body)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Error al actualizar perfil")
        }
    }




    suspend fun editPassword(id: Long, body: EditPasswordDtoReq) {
        val response = service.editPassword(id, body)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Error al cambiar contraseña")
        }
    }


    suspend fun deleteUser(id: Long) =
        service.deleteUser(id)

    suspend fun existsUserName(userName: String): Boolean {
        return service.existsUserName(userName)
    }

    //Post
    suspend fun createPost(
        context: Context,
        req: CreatePostDtoReq,
        imageUri: Uri
    ) = service.createPost(
        Gson().toJson(req).toRequestBody(JSON_MEDIA_TYPE.toMediaType()),
        uriToMultipart(context, imageUri)
    )


    suspend fun createAdoption(
        context: Context,
        req: CreateAdoptionPostDtoReq,
        imageUri: Uri
    ) = service.createAdoptionPost(
        Gson().toJson(req).toRequestBody(JSON_MEDIA_TYPE.toMediaType()),
        uriToMultipart(context, imageUri)
    )


    suspend fun getPostDetail(id: Long) =
        service.getPostDetail(id)

    suspend fun getAllPosts() =
        service.getAllPosts()

    suspend fun getPostsByCategory(type: String) =
        service.getPostsByCategory(type)

    suspend fun getPostsByAnimal(animal: String) =
        service.getPostsByAnimal(animal)

    suspend fun deletePost(id: Long) =
        service.deletePost(id)


    //Advice
    suspend fun createAdvice(
        context: Context,
        req: CreateAdviceDtoReq,
        imageUri: Uri
    ) = service.createAdvice(
        Gson().toJson(req).toRequestBody(JSON_MEDIA_TYPE.toMediaType()),
        uriToMultipart(context, imageUri)
    )


    suspend fun getAdviceDetail(id: Long) =
        service.getAdviceDetail(id)

    suspend fun getAllAdvices() =
        service.getAllAdvices()

    suspend fun getAdvicesByCategory(type: String) =
        service.getAdvicesByCategory(type)

    suspend fun deleteAdvice(id: Long) =
        service.deleteAdvice(id)

}