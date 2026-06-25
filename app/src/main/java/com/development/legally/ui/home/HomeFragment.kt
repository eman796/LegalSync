package com.development.legally.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.development.legally.R
import com.development.legally.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar navegación para los botones del menú principal
        binding.cardClients.setOnClickListener {
            findNavController().navigate(R.id.clientsFragment)
        }

        binding.cardCases.setOnClickListener {
            findNavController().navigate(R.id.casesFragment)
        }

        binding.cardReminders.setOnClickListener {
            findNavController().navigate(R.id.remindersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
