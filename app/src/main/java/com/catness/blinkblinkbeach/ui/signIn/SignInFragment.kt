package com.catness.blinkblinkbeach.ui.signIn

import android.app.AlertDialog
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
import com.catness.blinkblinkbeach.databinding.DialogForgotPasswordBinding
import com.catness.blinkblinkbeach.databinding.FragmentSignInBinding
import com.catness.blinkblinkbeach.utilities.exhaustive
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
                // inflate alert dialog box
                val customDialogBinding = DialogForgotPasswordBinding.inflate(layoutInflater)
                // build and show alert dialog box
                val builder = AlertDialog.Builder(activity)
                    .setView(customDialogBinding.root)
                val alertDialog = builder.show()
                // view binding
                customDialogBinding.apply {
                    dialogCancelButton.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    dialogSendEmailButton.setOnClickListener {
                        alertDialog.dismiss()
                        viewModel.onSendForgetEmailButtonClick(
                            dialogEmailEditText.text.toString()
                        )
                    }
                }
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
                        is SignInViewModel.SignInEvent.ShowToastMessage -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                        }
                    }.exhaustive
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
                    }.exhaustive
                }
            }
        }
    }
}