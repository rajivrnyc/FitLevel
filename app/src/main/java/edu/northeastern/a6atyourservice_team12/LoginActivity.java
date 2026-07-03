// Activity navigation from:
// https://developer.android.com/guide/components/intents-filters

package edu.northeastern.a6atyourservice_team12;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.a6atyourservice_team12.firebase.FirebaseHelper;
import edu.northeastern.a6atyourservice_team12.util.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private Button loginButton;
    private SessionManager sessionManager;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        firebaseHelper = FirebaseHelper.getInstance();


        if (sessionManager.isLoggedIn()) {
            goToStickerHome();
            return;
        }

        usernameInput = findViewById(R.id.editTextUsername);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String username = usernameInput.getText().toString().trim();

        if (username.isEmpty()) {
            usernameInput.setError("Please enter a username");
            return;
        }

        loginButton.setEnabled(false);

        firebaseHelper.loginUser(username, new FirebaseHelper.LoginCallback() {
            @Override
            public void onSuccess(String loggedInUsername) {
                sessionManager.saveUsername(loggedInUsername);
                Toast.makeText(LoginActivity.this,
                        "Welcome, " + loggedInUsername + "!",
                        Toast.LENGTH_SHORT).show();
                goToStickerHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                loginButton.setEnabled(true);
                Toast.makeText(LoginActivity.this,
                        "Login failed: " + errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToStickerHome() {
        Intent intent = new Intent(LoginActivity.this, SendStickersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
