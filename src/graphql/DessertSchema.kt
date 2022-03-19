package com.example.graphql

import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.example.models.Dessert
import com.example.models.DessertInput
import com.example.models.User
import com.example.services.DessertService

fun SchemaBuilder.dessertSchema(dessertService: DessertService) {

    inputType<DessertInput> {
        description = "The input of the dessert without the identifier"
    }

    type<Dessert> {
        description = "Dessert object with attributes name, description and imageUrl"
    }

    query("dessert") {
        resolver { dessertId: String ->
            try {
                dessertService.getDessert(dessertId)
            } catch (e: Exception) {
                null
            }
        }
    }

    query("desserts") {
        resolver { page: Int?, size: Int? ->
            try {
                dessertService.getDessertPage(page ?: 0, size ?: 10)
            } catch (e: Exception) {
                null
            }
        }
    }

    mutation("createDessert") {
        description = "Create a new dessert"
        resolver { ctx: Context, dessertInput: DessertInput ->
            try {
                val userId = ctx.get<User>()?.id ?: throw error("Not signed in")
                dessertService.createDessert(dessertInput, userId)
            } catch (e: Exception) {
                null
            }
        }
    }

    mutation("updateDessert") {
        description = "Update an existing dessert"
        resolver { ctx: Context ,dessertId: String, dessertInput: DessertInput ->
            try {
                val userId = ctx.get<User>()?.id ?: throw error("Not signed in")
                dessertService.updateDessert(userId, dessertId, dessertInput)
            } catch (e: Exception) {
                null
            }
        }
    }

    mutation("deleteDessert") {
        resolver { ctx: Context, dessertId: String ->
            try {
                val userId = ctx.get<User>()?.id ?: throw error("Not signed in")
                dessertService.deleteDessert(userId, dessertId)
            }catch (e: Exception) {
                null
            }
        }
    }
}