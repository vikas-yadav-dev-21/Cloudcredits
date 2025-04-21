package com.technicalsad.currencyconverter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputEditText amounteditbox;
    Spinner fromspinner, tospinner;
    TextView resultamount;
    Button convertbtn;

    double fromvalue=0.0, tovalue=0.0;
    String fromunit="", tounit="";

    Dialog dialogbox;

    private List<String> units= Arrays.asList(
            "AED", "AUD", "BHD", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EGP", "EUR", "GHS",
            "GBP", "HKD", "HUF", "IDR", "ILS", "INR", "JPY", "JOD", "KES", "KRW", "KWD", "MAD", "MYR", "MXN", "NGN", "NOK",
            "NZD", "OMR", "PHP", "PKR", "PLN", "QAR", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TZS", "USD", "VND", "ZAR"
    );

    private  List<Double> conversionValues=Arrays.asList(
            3.6669, 1.5686, 0.3763, 5.7974, 1.3836, 0.8165, 7.2868, 21.9812, 6.5522, 51.0344, 0.8777, 12.3215, 0.7521, 7.7636, 358.364,
            16832.3, 3.6719, 85.2152, 142.147, 0.7081, 129.271, 1414.29, 0.3061, 9.2940, 4.3930, 19.6835, 1606.9, 10.4894, 1.6838,
            0.3844, 56.6501, 279.921, 3.7737, 3.6410, 82.0246, 3.7428, 9.6322, 1.3109, 33.2815, 38.1818, 2032.17, 1.0000, 25820.7, 18.8285

    );

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
                window.setStatusBarColor(this.getResources().getColor(R.color.red));
            }

            amounteditbox=findViewById(R.id.amounteditbox);
            fromspinner=findViewById(R.id.fromspinner);
            tospinner=findViewById(R.id.tospinner);
            resultamount=findViewById(R.id.resultamount);
            convertbtn=findViewById(R.id.convertbtn);

            dialogbox=new Dialog(MainActivity.this);
            dialogbox.setContentView(R.layout.dialogbox);
            dialogbox.setCancelable(false);
            if (dialogbox.getWindow()!=null){
                dialogbox.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            initComponents();


            return insets;
        });
    }

    private void initComponents(){
        Collections.sort(units);
        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,units);
        tospinner.setAdapter(arrayAdapter);
        fromspinner.setAdapter(arrayAdapter);

        convertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getvalues
                if(amounteditbox.getText().toString().isEmpty()){
                    TextView dialogtxt=dialogbox.findViewById(R.id.dialogtxt);
                    Button dialogbtn=dialogbox.findViewById(R.id.dialogbtn);
                    dialogtxt.setText("All fields are required");
                    dialogbox.show();
                    dialogbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogbox.dismiss();
                        }
                    });

                }
                else{
                    fromvalue=Double.parseDouble(amounteditbox.getText().toString().trim());
                    fromunit=fromspinner.getSelectedItem().toString();
                    tounit=tospinner.getSelectedItem().toString();
                    convertvalue();
                }
            }
        });
    }
    private void convertvalue(){
        int index;
        double fromconversionrate, toconversionrate, result;
        if (fromunit==tounit){
            TextView dialogtxt=dialogbox.findViewById(R.id.dialogtxt);
            Button dialogbtn=dialogbox.findViewById(R.id.dialogbtn);
            dialogtxt.setText("Please select different currency, both currency can't same");
            dialogbox.show();
            dialogbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogbox.dismiss();
                }
            });

        }
        else {
            index=units.indexOf(fromunit);
            fromconversionrate=conversionValues.get(index);
            index=units.indexOf(tounit);
            toconversionrate=conversionValues.get(index);

            result=fromvalue/fromconversionrate;
            result=result*toconversionrate;
            resultamount.setText(String.valueOf(result)+" " +tounit);

        }
    }

}