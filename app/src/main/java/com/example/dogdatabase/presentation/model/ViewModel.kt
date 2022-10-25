package com.example.dogdatabase.presentation.model

import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import com.example.dogdatabase.data.database.Dogs
import com.example.dogdatabase.data.database.DogsDao
import kotlinx.coroutines.launch

class ViewModel(private val dogsDao: DogsDao) : ViewModel() {

    val allDogs: LiveData<List<Dogs>> = dogsDao.getAll().asLiveData()

//    fun updateDog(
//        id: Long,
//        breedOfDog: String,
//        nickName: String,
//        hostName: String,
//        address: String
//    ) {
//        val updatedDog = getUpdateDog(
//            id,
//            breedOfDog,
//            nickName,
//            hostName,
//            address)
//        updateDog(updatedDog)
//    }

//    private fun updateDog(dogs: Dogs) {
//        viewModelScope.launch {
//            dogsDao.update(dogs)
//        }
//    }
//
//    fun retrieveDog(id: Long): LiveData<Dogs> {
//        return dogsDao.loadAllById(id).asLiveData()
//    }

    fun addNewDog(
        breedOfDog: String,
        nickName: String,
        hostName: String,
        address: String) {
        val newDog = getNewDog(
            breedOfDog = breedOfDog,
            nickName = nickName,
            hostName = hostName,
            address = address)
        insertDog(newDog)
    }

    private fun insertDog(item: Dogs) {
        viewModelScope.launch {
            dogsDao.insertAll(item)
        }
    }

//    fun delete(dogs: Dogs) {
//        viewModelScope.launch {
//            dogsDao.delete(dogs)
//        }
//    }

    private fun getNewDog(
        breedOfDog: String,
        nickName: String,
        hostName: String,
        address: String): Dogs {
        return Dogs(
            breedOfDog = breedOfDog,
            nickName = nickName,
            hostName = hostName,
            address = address
        )
    }

    private fun getUpdateDog(
        id: Long,
        breedOfDog: String,
        nickName: String,
        hostName: String,
        address: String
    ): Dogs {
        return Dogs(
            id = id,
            breedOfDog = breedOfDog,
            nickName = nickName,
            hostName = hostName,
            address = address)
    }
}

class ModelValidation(
    private val dogsDao: DogsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            @Suppress("UNCHECKED_DOGS")
            return ViewModel(dogsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
