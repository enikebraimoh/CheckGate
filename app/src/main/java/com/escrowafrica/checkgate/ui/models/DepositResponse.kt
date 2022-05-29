package com.escrowafrica.checkgate.ui.models

data class DepositData(
    val amount: Int
)

data class DepositResponse(
    val data: DepositResponseData,
    val message: String,
    val success: Boolean
)

data class DepositResponseData(
    val link: String,
    val page: String
)