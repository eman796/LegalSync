package com.development.legally.ui.followups

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.development.legally.R
import com.development.legally.data.model.FollowUp
import java.text.SimpleDateFormat
import java.util.*

class FollowUpFormFragment : Fragment(R.layout.fragment_followup_form) {

    private val viewModel: FollowUpViewModel by viewModels()
    private var caseId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        caseId = arguments?.getString("caseId")

        val etDesc = view.findViewById<EditText>(R.id.et_description)
        val btnSave = view.findViewById<Button>(R.id.btn_save_followup)

        btnSave.setOnClickListener {
            val now = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val followUp = FollowUp(
                id = "",
                caseId = caseId ?: "",
                date = now,
                description = etDesc.text.toString(),
                responsibleUser = ""
            )

            viewModel.createFollowUp(followUp) { ok, err ->
                if (ok) requireActivity().onBackPressed()
                else Toast.makeText(requireContext(), err ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
