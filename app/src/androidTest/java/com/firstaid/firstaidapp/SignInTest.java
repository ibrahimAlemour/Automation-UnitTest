package com.firstaid.firstaidapp;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.firstaid.firstaidapp.databinding.ActivitySignInBinding;
import com.firstaid.firstaidapp.registration.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignInTest {

    @Rule
    public ActivityTestRule<SignInActivity> activityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void testUiAutomatorAPI() throws UiObjectNotFoundException, InterruptedException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

            //enter username
            UiSelector usernameSelector = new UiSelector().className("android.widget.EditText").text("Email");
            UiObject usernameWidget = device.findObject(usernameSelector);
            usernameWidget.setText("deve.2022.8@gmail.com");
            Thread.sleep(2000);

            //enter Password
            UiSelector PasswordSelector = new UiSelector().className("android.widget.EditText").text("Password");
            UiObject PasswordWidget = device.findObject(PasswordSelector);
            PasswordWidget.setText("123456");
            Thread.sleep(2000);

            UiSelector buLoginSelector = new UiSelector().className("android.widget.Button").text("Sign In").clickable(true);
            UiObject buLoginWidget = device.findObject(buLoginSelector);
            buLoginWidget.click();

            Thread.sleep(2000);
        }

}
