package net.iessochoa.vanesa.petcare.data.api.repository

import net.iessochoa.vanesa.petcare.data.model.wrapper.mapper.post.toDomain
import net.iessochoa.vanesa.petcare.model.Post

//Se crea pq hay q transformar, en advice no hacia falta pq el dto ya devolvía lo q necesito
object PostRepository {

    suspend fun getAllPosts(): List<Post> {
        val response = PetCareRepository.getAllPosts()   //PageResponse<AllPostsDto>
        return response.content.map { it.toDomain() }    //Convertimos a Post
    }

    suspend fun getPostDetail(id: Long): Post {
        val dto = PetCareRepository.getPostDetail(id)
        return dto.toDomain()
    }
}