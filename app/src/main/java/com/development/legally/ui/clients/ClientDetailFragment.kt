package com.development.legally.ui.clients

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.development.legally.R
import com.development.legally.data.model.Client

class ClientDetailFragment : Fragment(R.layout.fragment_client_detail) {

    private val viewModel: ClientViewModel by viewModels()
    private var clientId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clientId = arguments?.getString("clientId")

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)

        if (!clientId.isNullOrEmpty()) {
            viewModel.loadClientById(clientId!!) { client ->
                client?.let {
                    tvName.text = "${it.name} ${it.lastName}".trim()
                    tvPhone.text = it.phone
                    tvEmail.text = it.email
                }
            }
        }

        btnEdit.setOnClickListener {
            val bundle = bundleOf("clientId" to clientId)
            findNavController().navigate(R.id.clientFormFragment, bundle)
        }

        btnDelete.setOnClickListener {
            clientId?.let { id ->
                viewModel.deleteClient(id) { ok, err ->
                    if (ok) requireActivity().onBackPressed()
                    else Toast.makeText(requireContext(), err ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
