package com.amazing.ebookapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.amazing.ebookapp.databinding.ActivityMainBinding
import com.amazing.ebookapp.repository.AuthRepository
import com.amazing.ebookapp.repository.DataRepository
import com.amazing.ebookapp.viewmodel.AuthVMFactory
import com.amazing.ebookapp.viewmodel.AuthViewModel
import com.amazing.ebookapp.viewmodel.DataVMFactory
import com.amazing.ebookapp.viewmodel.DataViewModel
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toolbarTitle: TextView
    private lateinit var dataViewModel: DataViewModel
    private lateinit var authViewModel: AuthViewModel

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        toolbarTitle = binding.toolbarTitle

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.main,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.main.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.admin -> {
                    if (auth.currentUser != null) {
                        findNavController(R.id.fragmentContainerView).navigate(R.id.adminFragment)
                    } else {
                        findNavController(R.id.fragmentContainerView).navigate(R.id.adminLoginFragment)
                    }
                    binding.main.closeDrawers()
                    true
                }

                else -> false
            }
        }


        val dataRepository = DataRepository()
        dataViewModel = ViewModelProvider(this, DataVMFactory(dataRepository))[DataViewModel::class]

        val authRepository = AuthRepository()
        authViewModel = ViewModelProvider(this, AuthVMFactory(authRepository))[AuthViewModel::class]


    }
}