package com.firstaid.firstaidapp.registration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.app.ProgressDialog;
import android.widget.EditText;

import com.firstaid.firstaidapp.R;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignInActivityTest {

    private SignInActivity activity;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressDialog pd;
    private FirebaseAuth mAuth;

    @Before
    public void setUp() throws Exception {

        // Initialize the activity and views
        activity = new SignInActivity();
        etEmail = activity.findViewById(R.id.etEmail);
        etPassword = activity.findViewById(R.id.etPassword);
        pd = mock(ProgressDialog.class);
        mAuth = mock(FirebaseAuth.class);

        // Set the mocked views and FirebaseAuth object on the activity
        activity.etEmail = etEmail;
        activity.etPassword = etPassword;
        activity.pd = pd;
        activity.mAuth = mAuth;

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void signInTest(){

    }

    @Test
    public void testEmailEmpty_shouldSetError() {
        // Arrange
        String email = "";
        String password = "password";

        // Act
        activity.signInAccount(email, password);

        // Assert
        assertEquals("Email is Required.", etEmail.getError());
    }

    @Test
    public void testPasswordEmpty_shouldSetError() {
        // Arrange
        String email = "email@example.com";
        String password = "";

        // Act
        activity.signInAccount(email, password);

        // Assert
        assertEquals("Password is Required.", etPassword.getError());
    }

    @Test
    public void testPasswordTooShort_shouldSetError() {
        // Arrange
        String email = "email@example.com";
        String password = "1234";

        // Act
        activity.signInAccount(email, password);

        // Assert
        assertEquals("Password Must be >= 6 Characters", etPassword.getError());
    }

}