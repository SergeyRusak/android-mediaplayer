package com.sergeyrusak.mediaplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object MyViewModel : ViewModel() {
    val currentLength = MutableLiveData<Int>().apply { value = 0 }
    fun getLengthSingleton (): LiveData<Int> = currentLength as LiveData<Int>
    val currentPosition = MutableLiveData<Int>().apply { value = 0 }
    fun getPositionSingleton (): LiveData<Int> = currentPosition as LiveData<Int>
    val currentStatus = MutableLiveData<Boolean>().apply { value = false}
    fun getStatusSingleton (): LiveData<Boolean> = currentStatus as LiveData<Boolean>
    val currentCreated = MutableLiveData<Boolean>().apply { value = false}
    fun getCreatedSingleton (): LiveData<Boolean> = currentCreated as LiveData<Boolean>
}

