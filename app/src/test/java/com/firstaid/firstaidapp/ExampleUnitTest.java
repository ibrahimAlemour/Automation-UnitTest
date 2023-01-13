package com.firstaid.firstaidapp;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import com.firstaid.firstaidapp.registration.SignInActivity;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_SignIn() throws Exception{
        SignInActivity sign = new SignInActivity();
        //sign.signInAccount("deve.2022.8@gmail.com","123456");
       // Assert.assertTrue(sign.signInAccount("deve.2022.8@gmail.com","123456"));

    }
}