package com.amazing.ebookapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.amazing.ebookapp.MainActivity
import com.amazing.ebookapp.R
import com.amazing.ebookapp.databinding.FragmentWebViewBinding
import com.amazing.ebookapp.utils.ResultState
import com.amazing.ebookapp.viewmodel.DataViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private var _binding : FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel : DataViewModel by activityViewModels()
    private val args : WebViewFragmentArgs by navArgs()
    private lateinit var bookUrl : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpWebView()
        initializeWebView()

    }

    private fun initializeWebView() {
        binding.webView.loadUrl(args.bookItem.bookUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
    }


    override fun onResume() {
        super.onResume()

        (activity as MainActivity).toolbarTitle.text = "Enjoy the book"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}