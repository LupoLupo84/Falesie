package com.example.falesie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.example.falesie.data.room.FalesiaRepository
import com.example.falesie.data.room.ViaRepository
import javax.inject.Inject

class FalesieViewModelFactory @Inject constructor(private val repositoryFalesie: FalesiaRepository, private val repositoryVie: ViaRepository) : NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FalesieViewModel(repositoryFalesie, repositoryVie) as T

}