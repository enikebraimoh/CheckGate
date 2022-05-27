package com.escrowafrica.checkgate.ui.models

data class BasketsResponse(
    val data: Data,
    val message: String,
    val status: String
)

data class Data(
    val baskets: List<Basket>
)

data class Basket(
    val __v: Int,
    val _id: String,
    val amount: String,
    val basket_Id: String,
    val createdAt: String,
    val description: String,
    val owner: String,
    val paid: Boolean,
    val title: String,
    val updatedAt: String,
    val url: String
)