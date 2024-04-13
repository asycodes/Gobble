package com.sutd.t4app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.MainActivity;
import com.sutd.t4app.R;
import com.sutd.t4app.SignUpActivity;
import com.sutd.t4app.databinding.FragmentLoginBinding;
import com.sutd.t4app.databinding.FragmentSignUpBinding;
import com.sutd.t4app.ui.signup.SignUpFragmentViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginFragmentViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText email = root.findViewById(R.id.emailEditText);
        TextInputLayout emailTextInputLayout = root.findViewById(R.id.emailTextInputLayout);

        EditText pass = root.findViewById(R.id.passwordEditText);
        TextInputLayout passTextInputLayout = root.findViewById(R.id.passwordTextInputLayout);
        Button signup = root.findViewById(R.id.sign_up_button);
        Button submit = root.findViewById(R.id.signInButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loginProcess(email.getText().toString(),pass.getText().toString());
            }
        });

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        viewModel.getNavigationTrigger().observe(getViewLifecycleOwner(), nextFragmentTag -> {
            if (nextFragmentTag != null && !nextFragmentTag.isEmpty()) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        viewModel.getEmailError().observe(getViewLifecycleOwner(), emailError -> {
            emailTextInputLayout.setError(emailError);

        });
        viewModel.getPasswordError().observe(getViewLifecycleOwner(), passError -> {
            passTextInputLayout.setError(passError);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.cleanUp();
        binding = null;

    }


}
