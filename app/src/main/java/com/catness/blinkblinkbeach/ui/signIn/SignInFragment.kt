package com.catness.blinkblinkbeach.ui.signIn

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
import com.catness.blinkblinkbeach.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignInBinding.bind(view)

        binding.apply {
            signInButton.setOnClickListener {
                viewModel.onSignInButtonClick()
            }

            signInEmailEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.email.value = text?.toString() ?: ""
            }

            signInPasswordEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.password.value = text?.toString() ?: ""
            }

            forgetPasswordButton.setOnClickListener {
                // todo trigger dialogue box and ask for email
            }

            navigateToSignUpButton.setOnClickListener {
                viewModel.onNavigateToSignUpButtonClick()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.signInEvent.collect { event ->
                    when (event) {
                        is SignInViewModel.SignInEvent.NavigateToSignUpFragment -> {
                            val action =
                                SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.authState.collect { event ->
                    when (event) {
                        is AuthState.Error -> {
                            // hide the circular progress indicator
                            signInProgressCardView.visibility = View.INVISIBLE
                            // display error message as toast message
                            Toast.makeText(
                                this@SignInFragment.requireActivity(),
                                event.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is AuthState.Loading -> {
                            // show the circular progress indicator
                            signInProgressCardView.visibility = View.VISIBLE
                        }
                        is AuthState.Success -> {
                            // hide the circular progress indicator
                            signInProgressCardView.visibility = View.INVISIBLE
                            // navigate to the main page
                            val action =
                                SignInFragmentDirections.actionSignInFragmentToHomeFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }
}