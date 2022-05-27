package com.escrowafrica.checkgate.ui.models

data class WalletResponse(
    val data: WalletResponseData,
    val message: String,
    val success: Boolean
)

data class WalletResponseData(
    val alias: String,
    val balance: Int,
    val currency: String,
    val id: String,
    val limit: Any,
    val limits: Any,
    val on_hold_balance: Int,
    val received_balance: Int,
    val reserve_balance: Int
)