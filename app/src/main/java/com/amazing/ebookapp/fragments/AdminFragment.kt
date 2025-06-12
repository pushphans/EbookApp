package com.amazing.ebookapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazing.ebookapp.MainActivity
import com.amazing.ebookapp.R
import com.amazing.ebookapp.adapter.DataAdapter
import com.amazing.ebookapp.databinding.FragmentAdminBinding
import com.amazing.ebookapp.utils.ResultState
import com.amazing.ebookapp.viewmodel.AuthViewModel
import com.amazing.ebookapp.viewmodel.DataViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AdminFragment : Fragment(R.layout.fragment_admin) {

    private var _binding : FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private val authViewModel : AuthViewModel by activityViewModels()
    private val dataViewModel : DataViewModel by activityViewModels()
    private lateinit var dataAdapter : DataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewModel.getBooks()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().popBackStack(R.id.homeFragment, false)
        }


        dataAdapter = DataAdapter(onClick ={ onClick ->
            val action = AdminFragmentDirections.actionAdminFragmentToWebViewFragment(onClick)
            findNavController().navigate(action)
        },
            longOnClick = {longOnclick ->
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Delete Book")
                dialog.setMessage("Are you sure you wanna delete this book")
                dialog.setIcon(R.drawable.baseline_delete_24)
                dialog.setPositiveButton("Yes") { DialogInterface, which ->
                    dataViewModel.deleteBook(longOnclick)
                    dataViewModel.getBooks()
                    Toast.makeText(
                        requireContext(),
                        "Book deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                dialog.setNegativeButton("No"){DialogInterface, which ->
                    Toast.makeText(
                        requireContext(),
                        "Book not deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                dialog.setNeutralButton("Edit"){DialogInterface, which ->
                    val action = AdminFragmentDirections.actionAdminFragmentToEditBookFragment(longOnclick)
                    findNavController().navigate(action)
                }
                val alertDialog = dialog.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

            }

            )
        binding.adminRv.adapter = dataAdapter
        binding.adminRv.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            dataViewModel.getState.collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }
                    is ResultState.Error -> {
                        binding.progressBar2.visibility = View.GONE
                        binding.adminRv.visibility = View.GONE
                    }
                    is ResultState.Success -> {
                        binding.progressBar2.visibility = View.GONE
                        binding.adminRv.visibility = View.VISIBLE
                        dataAdapter.submitList(it.data)
                    }

                    else -> Unit

                }
            }
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.signOut()
            findNavController().navigate(R.id.homeFragment)
            authViewModel.resetAuthState()
        }


        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.addBookFragment)
        }










    }








    override fun onResume() {
        super.onResume()

        (activity as MainActivity).toolbarTitle.text = "Admin"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}