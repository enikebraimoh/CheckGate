package com.escrowafrica.checkgate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrowafrica.checkgate.ui.models.LoginRequest
import com.escrowafrica.checkgate.ui.models.LoginResponse
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

}