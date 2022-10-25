package com.example.dogdatabase.data.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.dogdatabase.data.database.Dogs
import com.example.dogdatabase.databinding.ItemBinding

class ViewHolder(
    private var binding: ItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dogs: Dogs) {
        binding.id.text = dogs.id.toString()
        binding.breed.text = dogs.breedOfDog
        binding.nickname.text = dogs.nickName
        binding.host.text = dogs.hostName
        binding.address.text = dogs.address
    }
}