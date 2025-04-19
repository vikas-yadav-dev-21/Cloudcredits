package com.technicalsad.passwordgenerator;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button generateButton;
    CheckBox uppercheckBox, lowercheckBox, numbercheckBox, symbolcheckBox;
    TextInputEditText lengtheditbox;
    TextView passwordtxt;

    int length=0;

    Dialog dialogbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.purple));
            }


            uppercheckBox=findViewById(R.id.uppercheckBox);
            lowercheckBox=findViewById(R.id.lowercheckBox);
            numbercheckBox=findViewById(R.id.numbercheckBox);
            symbolcheckBox=findViewById(R.id.symbolcheckBox);
            generateButton=findViewById(R.id.generatebutton);
            lengtheditbox=findViewById(R.id.lengtheditbox);
            passwordtxt=findViewById(R.id.password);


            dialogbox=new Dialog(MainActivity.this);
            dialogbox.setContentView(R.layout.dialogbox);
            dialogbox.setCancelable(false);
            if (dialogbox.getWindow()!=null){
                dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            generateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lengtheditbox.getText().toString().isEmpty()){
                        TextView dialogtxt=dialogbox.findViewById(R.id.dialogtxt);
                        Button dialogbtn=dialogbox.findViewById(R.id.dialogbtn);
                        dialogtxt.setText("Please enter password length");
                        dialogbox.show();
                        dialogbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogbox.dismiss();
                            }
                        });
                    }
                    else{
                        length=Integer.parseInt(lengtheditbox.getText().toString().trim());
                        generatepassword();
                    }

                }
            });



            return insets;
        });
    }

    public void generatepassword(){

        Random random = new Random();
        // Character sets
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-_=+<>?";

        // Validate selection
        List<String> charTypes = new ArrayList<>();
        if (uppercheckBox.isChecked()) charTypes.add(upperCaseLetters);
        if (lowercheckBox.isChecked()) charTypes.add(lowerCaseLetters);
        if (numbercheckBox.isChecked()) charTypes.add(numbers);
        if (symbolcheckBox.isChecked()) charTypes.add(specialChars);

        int typeCount = charTypes.size();

        if (typeCount == 0) {
            TextView dialogtxt=dialogbox.findViewById(R.id.dialogtxt);
            Button dialogbtn=dialogbox.findViewById(R.id.dialogbtn);
            dialogtxt.setText("At least one character type must be selected.");
            dialogbox.show();
            dialogbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogbox.dismiss();
                }
            });
        }
        else{
            // Calculate count per type
            int baseCount = length / typeCount;
            int remaining = length % typeCount;

            ArrayList<Character> passwordChars = new ArrayList<>();

            // Add base count characters for each selected type
            for (String charSet : charTypes) {
                for (int i = 0; i < baseCount; i++) {
                    passwordChars.add(randomChar(charSet, random));
                }
            }

            // Distribute remaining characters randomly among types
            for (int i = 0; i < remaining; i++) {
                String charSet = charTypes.get(random.nextInt(typeCount));
                passwordChars.add(randomChar(charSet, random));
            }

            // Shuffle the final list
            Collections.shuffle(passwordChars);

            // Build final password string
            StringBuilder password = new StringBuilder();
            for (char c : passwordChars) {
                password.append(c);
            }
            passwordtxt.setText(password);


        }

    }
    // Method to get a random character from a string
    private static char randomChar(String source, Random random) {
        return source.charAt(random.nextInt(source.length()));
    }

}
