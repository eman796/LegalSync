package com.development.legally.ui.clients

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.core.os.bundleOf
import com.development.legally.R
import com.development.legally.data.model.Client
import kotlinx.coroutines.launch

class ClientFormFragment : Fragment(R.layout.fragment_client_form) {

    private val viewModel: ClientViewModel by viewModels()
    private var clientId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clientId = arguments?.getString("clientId")

        val etName = view.findViewById<EditText>(R.id.et_name)
        val etLast = view.findViewById<EditText>(R.id.et_lastname)
        val etPhone = view.findViewById<EditText>(R.id.et_phone)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        if (!clientId.isNullOrEmpty()) {
            viewModel.loadClientById(clientId!!) { client ->
                client?.let {
                    etName.setText(it.name)
                    etLast.setText(it.lastName)
                    etPhone.setText(it.phone)
                    etEmail.setText(it.email)
                }
            }
        }

        btnSave.setOnClickListener {
            val client = Client(
                id = clientId ?: "",
                name = etName.text.toString(),
                lastName = etLast.text.toString(),
                phone = etPhone.text.toString(),
                email = etEmail.text.toString()
            )

            if (clientId.isNullOrEmpty()) {
                viewModel.createClient(client) { ok, err ->
                    if (ok) requireActivity().onBackPressed()
                    else Toast.makeText(requireContext(), err ?: "Error", Toast.LENGTH_SHORT).show()
                }
            } else {
                viewModel.updateClient(client) { ok, err ->
                    if (ok) requireActivity().onBackPressed()
                    else Toast.makeText(requireContext(), err ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
