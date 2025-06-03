package com.amazing.ebookapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amazing.ebookapp.MainActivity
import com.amazing.ebookapp.R
import com.amazing.ebookapp.databinding.FragmentLoginAdminBinding
import com.amazing.ebookapp.utils.ResultState
import com.amazing.ebookapp.viewmodel.AuthViewModel
import com.google.android.gms.common.internal.Objects.ToStringHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AdminLoginFragment : Fragment(R.layout.fragment_login_admin) {

    private var _binding: FragmentLoginAdminBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginAdminBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email == "pushp.hans1502@gmail.com" && password == "15022004") {

                    authViewModel.signIn(email, password)
                    binding.etEmail.text.clear()
                    binding.etPassword.text.clear()

                } else {
                    Toast.makeText(requireContext(), "Details are not valid", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.authState.collectLatest {
                when(it){
                    is ResultState.Loading ->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error occurred : ${it.error}", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Success ->{
                        binding.progressBar.visibility = View.GONE
                        if(it.data == true){
                            findNavController().navigate(R.id.adminFragment)
                        }
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()

        (activity as MainActivity).toolbarTitle.text = "Admin Login"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}