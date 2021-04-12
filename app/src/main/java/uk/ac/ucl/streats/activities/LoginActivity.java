package uk.ac.ucl.streats.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.InvalidPropertiesFormatException;

import uk.ac.ucl.streats.data.HillCipher;
import uk.ac.ucl.streats.R;

public class LoginActivity extends AppCompatActivity {

    public Button login_button;
    Button register_button;

    EditText email, password;
    FirebaseAuth fAuth;

    class OnCompleteSignInListener implements OnCompleteListener<AuthResult> {
        public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
            if (task.isSuccessful())
            {
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent (getApplicationContext(), HomeActivity.class));
                finish();
            }
            else {
                password.setError("Invalid password.");
                login_button.setEnabled(true);
                Toast.makeText(LoginActivity.this,"Error! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set up toolbar
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.top_banner_toolbar);
        setSupportActionBar(profileToolbar);

        login_button = (Button) findViewById(R.id.login_button);
        register_button  = (Button) findViewById(R.id.SignUp1);
        email = findViewById(R.id.username_register);
        password = findViewById(R.id.password_login);

        fAuth = FirebaseAuth.getInstance();
        login_button.setOnClickListener(v -> login());


        register_button.setOnClickListener(v -> goToRegister());
    }

    private void login() {
        login_button.setEnabled(false);

        String password = this.password.getText().toString().trim();
        String email = this.email.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            this.email.setError("Email is required.");
            login_button.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            this.password.setError("Password is required.");
            login_button.setEnabled(true);
            return;
        }

        HillCipher h = new HillCipher();
        try {
            password =  h.encrypt(password);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        }

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteSignInListener());
    }

    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


}