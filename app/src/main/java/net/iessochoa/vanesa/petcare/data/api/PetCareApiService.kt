package net.iessochoa.vanesa.petcare.data.api

import net.iessochoa.vanesa.petcare.data.model.dto.advice.AllAdvicesDto
import net.iessochoa.vanesa.petcare.data.model.dto.advice.DetailAdviceDto
import net.iessochoa.vanesa.petcare.data.model.dto.page.PageResponse
import net.iessochoa.vanesa.petcare.data.model.dto.post.AllPostsDto
import net.iessochoa.vanesa.petcare.data.model.dto.post.DetailPostDto
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.CreateUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.EditPasswordDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.EditUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.LoginUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.user.CreateUserDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.LoginUserDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilFiltradoDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PetCareApiService {

    //User
    @POST("user/login")
    suspend fun login(
        @Body body: LoginUserDtoReq
    ): LoginUserDto

    @POST("user/createUser")
    suspend fun createUser(
        @Body body: CreateUserDtoReq
    ): Response<CreateUserDto>

    @PATCH("user/editUser/{id}")
    suspend fun editUser(
        @Path("id") id: Long,
        @Body body: EditUserDtoReq
    ): Response<Void>

    @GET("user/getPerfil/{id}")
    suspend fun getPerfil(
        @Path("id") id: Long
    ): PerfilDto

    @PATCH("user/editPassword/{id}")
    suspend fun editPassword(
        @Path("id") id: Long,
        @Body body: EditPasswordDtoReq
    ): Response<Void>

    @GET("user/getPerfilFiltrado/{userId}")
    suspend fun getPerfilFiltrado(
        @Path("userId") userId: Long,
        @Query("type") type: String
    ): PerfilFiltradoDto

    @DELETE("user/deleteUser/{id}")
    suspend fun deleteUser(
        @Path("id") id: Long
    ): Response<Void>

    @GET("user/exists/userName/{userName}")
    suspend fun existsUserName(
        @Path("userName") userName: String
    ): Boolean



    //Posts
    @Multipart
    @POST("post/create")
    suspend fun createPost(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part
    ): DetailPostDto

    @Multipart
    @POST("post/create/adoption")
    suspend fun createAdoptionPost(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part
    ): DetailPostDto

    @GET("post/detail/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): DetailPostDto

    @GET("post/all")
    suspend fun getAllPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<AllPostsDto>

    @GET("post/allPost/category/{type}")
    suspend fun getPostsByCategory(
        @Path("type") type: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<AllPostsDto>

    @GET("post/allPost/animal/{animal}")
    suspend fun getPostsByAnimal(
        @Path("animal") animal: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<AllPostsDto>

    @DELETE("post/deletePost/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    ): Response<Void>


    //Advices
    @Multipart
    @POST("advice/createAdvice")
    suspend fun createAdvice(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part
    ): DetailAdviceDto

    @GET("advice/detail/{id}")
    suspend fun getAdviceDetail(
        @Path("id") id: Long
    ): DetailAdviceDto

    @GET("advice/all")
    suspend fun getAllAdvices(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<AllAdvicesDto>

    @GET("advice/allAdvice/category/{type}")
    suspend fun getAdvicesByCategory(
        @Path("type") type: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): PageResponse<AllAdvicesDto>

    @DELETE("advice/deleteAdvice/{id}")
    suspend fun deleteAdvice(
        @Path("id") id: Long
    ): Response<Void>
}
