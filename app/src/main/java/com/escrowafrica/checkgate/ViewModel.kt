package com.escrowafrica.checkgate

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrowafrica.checkgate.ui.models.Basket
import com.escrowafrica.checkgate.ui.models.LoginRequest
import com.escrowafrica.checkgate.ui.models.LoginResponse
import com.escrowafrica.checkgate.ui.models.SignUpRequest
import com.escrowafrica.checkgate.ui.models.SignUpResponse
import com.escrowafrica.checkgate.ui.models.WalletResponseData
import com.escrowafrica.checkgate.ui.network.SessionManager
import com.escrowafrica.checkgate.ui.util.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel
@Inject
constructor(val repo: Repository) : ViewModel() {

    @Inject
    lateinit var sessions: SessionManager

    private var _loginState = MutableSharedFlow<StateMachine<LoginResponse>>()
    val loginState: MutableSharedFlow<StateMachine<LoginResponse>> = _loginState

    private var _signUpState = MutableSharedFlow<StateMachine<SignUpResponse>>()
    val signUpState: MutableSharedFlow<StateMachine<SignUpResponse>> = _signUpState

    private val _baskets : MutableState<StateMachine<List<Basket>>> =
        mutableStateOf(StateMachine.Ideal)
    val baskets: State<StateMachine<List<Basket>>> = _baskets

    private val _wallet : MutableState<StateMachine<WalletResponseData>> =
        mutableStateOf(StateMachine.Ideal)
    val wallet: State<StateMachine<WalletResponseData>> = _wallet

    init {
        getBaskets()
        getWallet()
    }


    fun login(loginDetails: LoginRequest) {
        viewModelScope.launch {
            repo.login(loginDetails).collect {
                when (it) {
                    is StateMachine.Success<LoginResponse> -> {
                        sessions.saveAuthToken(it.data.jwt)
                        App.token = it.data.jwt
                    }
                    else -> {}
                }

                Log.d("",it.toString())
                _loginState.emit(it)
            }
        }
    }

    fun signUp(signUpDetails: SignUpRequest) {
        viewModelScope.launch {
            repo.signUp(signUpDetails).collect {
                _signUpState.emit(it)
            }
        }
    }

    fun getBaskets() {
        viewModelScope.launch {
            repo.getBaskets().collect {
                _baskets.value = it
            }
        }
    }

    fun getWallet() {
        viewModelScope.launch {
            repo.getBalance().collect {
                _wallet.value = it
            }
        }
    }

}