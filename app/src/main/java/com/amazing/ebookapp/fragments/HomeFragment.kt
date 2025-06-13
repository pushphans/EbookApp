package com.amazing.ebookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazing.ebookapp.MainActivity
import com.amazing.ebookapp.R
import com.amazing.ebookapp.adapter.DataAdapter
import com.amazing.ebookapp.databinding.FragmentHomeBinding
import com.amazing.ebookapp.utils.ResultState
import com.amazing.ebookapp.viewmodel.DataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataAdapter: DataAdapter
    private val dataViewModel: DataViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dataAdapter = DataAdapter(
            onClick = { onClicked ->
                val action = HomeFragmentDirections.actionHomeFragmentToWebViewFragment(onClicked)
                findNavController().navigate(action)
            },
            longOnClick = {
            Toast.makeText(requireContext(), it.bookName, Toast.LENGTH_SHORT).show()
            })

        binding.homeRv.adapter = dataAdapter
        binding.homeRv.layoutManager = LinearLayoutManager(requireContext())

        dataViewModel.getBooks()

        viewLifecycleOwner.lifecycleScope.launch {
            dataViewModel.getState.collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Unable to fetch books for you",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE
                        binding.homeRv.visibility = View.GONE
                    }

                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.homeRv.visibility = View.GONE
                    }

                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.homeRv.visibility = View.VISIBLE

                        dataAdapter.submitList(it.data)

                    }

                    else -> Unit
                }
            }
        }

        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterList(newText)
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        dataViewModel.getState.collectLatest {
                            when (it) {
                                is ResultState.Success ->
                                    dataAdapter.submitList(it.data)

                                else -> Unit
                            }
                        }
                    }
                }

                return true
            }
        })

    }

    private fun filterList(newText: String) {
        lifecycleScope.launch {
            val query = newText.replace(Regex("[^A-Za-z0-9]"), "")

            dataViewModel.getState.collectLatest {
                when (it) {
                    is ResultState.Success -> {
                        val list = it.data.filter { book ->
                            val cleanCategory =
                                book.category.replace(Regex("[^A-Za-z0-9]"), "").lowercase()
                            val cleanAuthor =
                                book.author.replace(Regex("[^A-Za-z0-9]"), "").lowercase()
                            val cleanBookName =
                                book.bookName.replace(Regex("[^A-Za-z0-9]"), "").lowercase()

                            cleanCategory.contains(query) || cleanAuthor.contains(query) || cleanBookName.contains(
                                query
                            )
                        }
                        dataAdapter.submitList(list)
                    }

                    else -> Unit
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).toolbarTitle.text = "Home"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}