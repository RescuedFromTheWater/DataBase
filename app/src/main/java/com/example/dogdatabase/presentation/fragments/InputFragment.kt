package com.example.dogdatabase.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dogdatabase.data.adapter.Adapter
import com.example.dogdatabase.data.database.Database
import com.example.dogdatabase.database
import com.example.dogdatabase.databinding.InputFragmentBinding
import com.example.dogdatabase.presentation.model.ViewModel
import com.example.dogdatabase.presentation.extensions.getTextOrSetError
import com.example.dogdatabase.presentation.model.ModelValidation
import com.google.android.material.textfield.TextInputLayout

class InputFragment : Fragment() {

    private var _binding: InputFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val dogsDao by lazy {
        requireContext().database.dogsDao
    }

    private val ViewModel: ViewModel by activityViewModels {
        ModelValidation(
            (activity?.application as Database).dogsDao)
    }

    private val adapter = Adapter {
        updateList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return InputFragmentBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateList()

        with(binding) {
            buttonAdd.setOnClickListener {
                val breedOfDog = breedOfDog.getTextOrSetError()
                val nickname = name.getTextOrSetError()
                val hostName = host.getTextOrSetError()
                val address = address.getTextOrSetError()
                if (breedOfDog == null || nickname == null ||
                    hostName == null || address == null
                ) return@setOnClickListener

                ViewModel.addNewDog(breedOfDog, nickname, hostName, address)
                updateList()
                breedOfDog.clear()
                nickname.clear()
                hostName.clear()
                address.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList() {
        ViewModel.allDogs.observe(viewLifecycleOwner) { dogs ->
            adapter.submitList(dogs)
        }
    }

    private fun TextInputLayout.clear(): String {
        return editText?.text?.clear().toString()
    }
}