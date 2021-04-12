package uk.ac.ucl.streats.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

import uk.ac.ucl.streats.data.HillCipher;
import uk.ac.ucl.streats.R;

public class RegisterActivity extends  AppCompatActivity{

    private EditText name, username, email, password;
    private Button register_button;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    class OnCompleteRegisterListener implements OnCompleteListener<AuthResult> {
        String email;
        String password;

        public OnCompleteRegisterListener(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful())
            {
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteSignInListener());
            }
            else {
                register_button.setEnabled(true);
                Toast.makeText(RegisterActivity.this,"Error! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class OnCompleteSignInListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful())
            {
                Map<String, Object> data = new HashMap<>();
                data.put("name", name.getText().toString());
                data.put("username", username.getText().toString());
                data.put("bio", "");
                data.put("favourites", new ArrayList<>());
                data.put("bookmarked", new ArrayList<>());


                String userID = task.getResult().getUser().getUid();
                db.collection("profiles").document(userID).set(data);

                Toast.makeText(RegisterActivity.this, "User Account Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent (getApplicationContext(), LoginActivity.class));
                finish();
            }
            else {
                Toast.makeText(RegisterActivity.this,"Error! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name_register);
        username = findViewById(R.id.username_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(v -> RegisterAction());

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    private void RegisterAction() {
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setEnabled(false);

        String password = this.password.getText().toString().trim();
        String email = this.email.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            this.email.setError("Email is required.");
            registerButton.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            this.password.setError("Password is required.");
            registerButton.setEnabled(true);
            return;
        }

        HillCipher h = new HillCipher();
        try {
            password =  h.encrypt(password);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        }

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteRegisterListener(email, password));
    }
}
