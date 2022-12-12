package com.catness.blinkblinkbeach.ui.signUp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.repositories.auth.AuthState
import com.catness.blinkblinkbeach.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignUpBinding.bind(view)

        binding.apply {

            signUpUsernameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.username.value = text?.toString() ?: ""
            }

            signUpEmailEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.email.value = text?.toString() ?: ""
            }

            signUpPasswordEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.password.value = text?.toString() ?: ""
            }

            signUpButton.setOnClickListener {
                viewModel.onSignUpButtonClick()
            }

            navigateToSignInButton.setOnClickListener {
                viewModel.onNavigateToSignInButtonClick()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.signUpEvent.collect { event ->
                    when (event) {
                        is SignUpViewModel.SignUpEvent.NavigateToSignInFragment -> {
                            findNavController().navigateUp()
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.authState.collect { event ->
                    when (event) {
                        is AuthState.Error -> {
                            // hide the circular progress indicator
                            signUpProgressCardView.visibility = View.INVISIBLE
                            // display error message as toast message
                            Toast.makeText(
                                this@SignUpFragment.requireActivity(),
                                event.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is AuthState.Loading -> {
                            // show the circular progress indicator
                            signUpProgressCardView.visibility = View.VISIBLE
                        }
                        is AuthState.Success -> {
                            // hide the circular progress indicator
                            signUpProgressCardView.visibility = View.INVISIBLE
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }
}