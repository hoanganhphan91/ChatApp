package com.linhkienrobotics.chatapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import com.linhkienrobotics.chatapp.databinding.ActivitySignUpBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.textSignIn.setOnClickListener(v->onBackPressed());
        binding.buttonSignUp.setOnClickListener(v-> {
            if (isValidSignUpDetails()){
                signUp();
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void signUp(){

    }
    private String encodeImage(Bitmap bitmap)
    {
        int previewWidth = 150;
        int previewHeight= bitmap.getHeight()+previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes= byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if(result.getResultCode()== RESULT_OK){
                    if (result.getData()!=null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                        }catch (FileNotFoundException  e )
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if (encodedImage == null){
            showToast("Select profile image");
            return false;
        }else if (binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Enter name:");
            return false;
        }else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Enter mail");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
        {
            showToast("Enter valid email");
            return false;
        }
        else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Enter password");
            return false;
        }
        else if (binding.inputConfirmedPassword.getText().toString().trim().isEmpty()){
            showToast("Confirm your password");
            return false;
        }
        else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmedPassword.getText().toString())){
            showToast(" Password haven't match with confirmed password");
            return false;
        }
        else {
            return true;
        }
    }

    private void loading (boolean isloading){
        if (isloading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }

    }
}