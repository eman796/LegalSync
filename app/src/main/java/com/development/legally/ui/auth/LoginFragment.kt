package com.development.legally.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.development.legally.R
import com.development.legally.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Credenciales de prueba temporales
        val testEmail = "admin@legally.com"
        val testPass = "admin123"

        // Autocompletar para facilitar las pruebas
        binding.etEmail.setText(testEmail)
        binding.etPassword.setText(testPass)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email == testEmail && pass == testPass) {
                // Navegar al Home si los datos son correctos
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Toast.makeText(requireContext(), "Credenciales incorrectas (Usa admin@legally.com / admin123)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}