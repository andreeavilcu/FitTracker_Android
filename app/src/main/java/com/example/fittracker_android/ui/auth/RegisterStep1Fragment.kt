package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fittracker_android.R

class RegisterStep1Fragment : Fragment(){
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nextButton: Button
    private val args: RegisterStep1FragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        nextButton = view.findViewById(R.id.nextButton)


        args.email?.let { emailEditText.setText(it) }
        args.password?.let { passwordEditText.setText(it) }

        nextButton.setOnClickListener {
            val email = emailEditText.text.toString()

            val action = RegisterStep1FragmentDirections.actionRegisterStep1ToRegisterStep2(email)
            findNavController().navigate(action)
        }
    }
}