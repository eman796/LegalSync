package com.development.legally.ui.followups

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
import kotlinx.coroutines.flow.collectLatest

class FollowUpsFragment : Fragment(R.layout.fragment_follow_ups) {

    private val viewModel: FollowUpViewModel by viewModels()
    private lateinit var adapter: FollowUpAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowUpAdapter()
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_followups)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_followup)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val caseId = arguments?.getString("caseId") ?: ""
        fab.setOnClickListener {
            val bundle = bundleOf("caseId" to caseId)
            findNavController().navigate(R.id.followUpFormFragment, bundle)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                adapter.submitList(state.followUps)
            }
        }

        if (caseId.isNotEmpty()) viewModel.loadFollowUps(caseId)
    }
}
