package com.amazing.ebookapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.amazing.ebookapp.MainActivity
import com.amazing.ebookapp.R
import com.amazing.ebookapp.databinding.FragmentAddBookBinding
import com.amazing.ebookapp.model.Books
import com.amazing.ebookapp.viewmodel.DataViewModel

class AddBookFragment : Fragment(R.layout.fragment_add_book) {

    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBookBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val bookName = binding.etBookName.text.toString()
            val author = binding.etAuthor.text.toString()
            val description = binding.etDescription.text.toString()
            val category = binding.etCategory.text.toString()
            val bookImageUrl = binding.etBookImageUrl.text.toString()
            val bookUrl = binding.etBookUrl.text.toString()


            if(bookName.isEmpty() || author.isEmpty() || description.isEmpty() || category.isEmpty() || bookImageUrl.isEmpty() || bookUrl.isEmpty()){
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                val book = Books(
                    bookName = bookName,
                    author = author,
                    description = description,
                    category = category,
                    bookImageUrl = bookImageUrl,
                    bookUrl = bookUrl
                )

                dataViewModel.addBooks(book)
                Toast.makeText(requireContext(), "Book saved successfully", Toast.LENGTH_SHORT).show()
                binding.etBookName.text.clear()
                binding.etAuthor.text.clear()
                binding.etDescription.text.clear()
                binding.etCategory.text.clear()
                binding.etBookImageUrl.text.clear()
                binding.etBookUrl.text.clear()
                Log.d("myTag", book.toString())
            }

        }

    }


    override fun onResume() {
        super.onResume()

        (activity as MainActivity).toolbarTitle.text = "Add Books"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}