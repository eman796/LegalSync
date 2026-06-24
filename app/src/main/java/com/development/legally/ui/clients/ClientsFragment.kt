package com.development.legally.ui.clients

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.legally.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ClientsFragment : Fragment(R.layout.fragment_clients) {

    private val viewModel: ClientViewModel by viewModels()
    private lateinit var adapter: ClientAdapter
    private val queryFlow = MutableSharedFlow<String>(replay = 1)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClientAdapter { client ->
            val bundle = bundleOf("clientId" to client.id)
            findNavController().navigate(R.id.clientDetailFragment, bundle)
        }

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_clients)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_client)
        val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        fab.setOnClickListener {
            findNavController().navigate(R.id.clientFormFragment)
        }

        // Collect clients state
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                adapter.submitList(state.filtered)
            }
        }

        // Search with debounce
        lifecycleScope.launch {
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean { return true }
                override fun onQueryTextChange(newText: String?): Boolean {
                    lifecycleScope.launch { queryFlow.emit(newText.orEmpty()) }
                    return true
                }
            })
        }

        lifecycleScope.launch {
            queryFlow.debounce(300).collectLatest { q ->
                viewModel.filter(q)
            }
        }

        // Initial load
        viewModel.loadClients()
    }
}
