package com.escrowafrica.checkgate.ui.models

data class UserWalletResponse(
    val data: UserWalletResponseData,
    val message: String,
    val success: Boolean
)

data class UserWalletResponseData(
    val balance: Int
)