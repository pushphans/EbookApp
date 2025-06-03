package com.amazing.ebookapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.amazing.ebookapp.MainActivity
import com.amazing.ebookapp.R
import com.amazing.ebookapp.databinding.FragmentEditBookBinding
import com.amazing.ebookapp.model.Books
import com.amazing.ebookapp.utils.ResultState
import com.amazing.ebookapp.viewmodel.DataViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class EditBookFragment : Fragment(R.layout.fragment_edit_book) {
    private var _binding: FragmentEditBookBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels()
    private val args: EditBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBookBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.editBook.id
        val bookName = args.editBook.bookName
        val author = args.editBook.author
        val description = args.editBook.description
        val category = args.editBook.category
        val imageUrl = args.editBook.bookImageUrl
        val bookUrl = args.editBook.bookUrl

        binding.etBookName.setText(bookName)
        binding.etAuthor.setText(author)
        binding.etDescription.setText(description)
        binding.etCategory.setText(category)
        binding.etBookImageUrl.setText(imageUrl)
        binding.etBookUrl.setText(bookUrl)

        binding.btnUpdate.setOnClickListener {
            val upName = binding.etBookName.text.toString()
            val upAuthor = binding.etAuthor.text.toString()
            val upDescription = binding.etDescription.text.toString()
            val upCategory = binding.etCategory.text.toString()
            val upBookImageUrl = binding.etBookImageUrl.text.toString()
            val upBookUrl = binding.etBookUrl.text.toString()

            val book = Books(
                id = id,
                bookName = upName,
                author = upAuthor,
                description = upDescription,
                category = upCategory,
                bookImageUrl = upBookImageUrl,
                bookUrl = upBookUrl
            )
            dataViewModel.updateBook(book)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            dataViewModel.updateState.collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        binding.progressBar3.visibility = View.VISIBLE
                    }

                    is ResultState.Error -> {
                        binding.progressBar3.visibility = View.GONE
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultState.Success -> {
                        binding.progressBar3.visibility = View.GONE
                       Toast.makeText(requireContext(), "Data updated", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbarTitle.text = "Edit Books"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}