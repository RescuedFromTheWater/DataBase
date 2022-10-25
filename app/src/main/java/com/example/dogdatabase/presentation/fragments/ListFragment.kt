package com.example.dogdatabase.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdatabase.DatabaseApplication
import com.example.dogdatabase.R
import com.example.dogdatabase.data.adapter.Adapter
import com.example.dogdatabase.data.database.Dogs
import com.example.dogdatabase.database
import com.example.dogdatabase.databinding.ListFragmentBinding
import com.example.dogdatabase.presentation.extensions.addVerticalGaps
import com.example.dogdatabase.presentation.model.ModelValidation
import com.example.dogdatabase.presentation.model.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ListFragment : Fragment() {

    private var _binding: ListFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val dogsDao by lazy {
        requireContext().database.dogsDao
    }

    private val ViewModel: ViewModel by activityViewModels {
        ModelValidation(
            (activity?.application as DatabaseApplication).database.dogsDao
        )
    }

    private val adapter by lazy {
        Adapter(
            onItemClicked = {
                editDialog()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ListFragmentBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerView.adapter = adapter

            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            swipeRefresh.setOnRefreshListener {
                updateList()
                swipeRefresh.isRefreshing = false
            }

            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addVerticalGaps()

            val dogs = dogsDao.getAllDog()

            with(toolbar) {
                menu
                    .findItem(R.id.search)
                    .actionView
                    .let { it as SearchView }
                    .setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(query: String): Boolean {
                            val filteredDogs = dogs
                                .filter {
                                    it.breedOfDog.contains(query) || it.nickName.contains(query) ||
                                            it.hostName.contains(query) || it.address.contains(query)
                                }
                            adapter.submitList(filteredDogs)
                            return true
                        }
                    })
            }

            val itemCallback = object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val dogs = adapter.currentList[position]
                    deleteDialog(view, dogs)

                }
            }
            ItemTouchHelper(itemCallback).apply {
                attachToRecyclerView(binding.recyclerView)
            }
        }
        updateList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList() {
        ViewModel.allDogs.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }

    private fun deleteDialog(dogs: Dogs) {
        return (activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(DIALOG_TEXT)
                .setCancelable(false)
                .setNegativeButton(NEGATIVE_BUTTON) { _, _ ->
                    Toast.makeText(
                        activity, TOAST_CANCEL,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setPositiveButton(POSITIVE_BUTTON) { _, _ ->
                    dogsDao.delete(dogs)
                    Toast.makeText(
                        activity, TOAST_DELETE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            builder.create()
        } ?: throw IllegalStateException(ILLEGAL_STATE_EXCEPTION)) as Unit
    }

    private fun editDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(EDIT_TEXT)
            .setCancelable(false)
            .setNegativeButton(NEGATIVE_BUTTON) { _, _ ->
            }
            .setPositiveButton(POSITIVE_BUTTON) { _, _ ->
                editItem()
            }
            .show()
    }

    private fun editItem() {
        view?.findNavController()?.navigate(R.id.action_global_action_data)
    }

    companion object {
        private const val DIALOG_TEXT = "Delete?"
        private const val POSITIVE_BUTTON = "Yes"
        private const val NEGATIVE_BUTTON = "No"
        private const val TOAST_DELETE = "Deleted successfully"
        private const val TOAST_CANCEL = "Deleting cancelled"
        private const val EDIT_TEXT = "Edit?"
        private const val ILLEGAL_STATE_EXCEPTION = "Error"
    }
}