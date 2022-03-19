package com.example.services

import com.example.models.Dessert
import com.example.models.DessertInput
import com.example.models.DessertsPage
import com.example.repository.DessertRepository
import com.example.repository.ReviewRepository
import com.mongodb.client.MongoClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.UUID

class DessertService: KoinComponent {
    private val client: MongoClient by inject()
    private val repo: DessertRepository = DessertRepository(client)
    private val reviewRepo: ReviewRepository = ReviewRepository(client)

    fun getDessertPage(page: Int, size: Int): DessertsPage {
        return repo.getDessertsPage(page, size)
    }

    fun getDessert(id: String): Dessert {
        val dessert = repo.getById(id)
        dessert.reviews = reviewRepo.getReviewsByDessertId(id)
        return dessert
    }

    fun createDessert(dessertInput: DessertInput, userId: String): Dessert {
        val uid = UUID.randomUUID().toString()
        val dessert = Dessert(
            id = uid,
            userId = userId,
            name = dessertInput.name,
            description = dessertInput.description,
            imageUrl = dessertInput.imageUrl
        )
        return repo.add(dessert)
    }

    fun updateDessert(userId: String, desserId: String, dessertInput: DessertInput): Dessert {
        val dessert = repo.getById(desserId)
        if (dessert.userId == userId) {
            val updates = Dessert(
                id = desserId,
                userId = userId,
                name = dessertInput.name,
                description = dessertInput.description,
                imageUrl = dessertInput.imageUrl
            )
            return repo.update(updates)
        }
        error("Cannot update dessert")
    }

    fun deleteDessert(userId: String, dessertId: String): Boolean {
        val dessert = repo.getById(dessertId)
        if (dessert.userId == userId) {
            return repo.delete(dessertId)
        }
        error("Cannot delete desert")
    }
}