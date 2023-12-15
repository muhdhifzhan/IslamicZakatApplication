package com.example.mymobileapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener
{

    Toolbar zakatToolbar;

    EditText GoldWeight, CurrentValue;
    RadioButton rbTypeKeep, rbTypeWear;
    Button btnCalculate, btnReset;
    TextView tvOutTotalGold, tvOutTotalPayable, tvOutTotalZakat;

    ImageView infoGoldRate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        zakatToolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(zakatToolbar);
        getSupportActionBar().setTitle("Gold Zakat Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GoldWeight = (EditText) findViewById(R.id.tvInGoldWeight);
        CurrentValue = (EditText) findViewById(R.id.tvInCurrentValue);
        rbTypeKeep = (RadioButton) findViewById(R.id.rbKeep);
        rbTypeWear = (RadioButton) findViewById(R.id.rbWear);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        tvOutTotalGold = (TextView) findViewById(R.id.tvOutTotalGold);
        tvOutTotalPayable = (TextView) findViewById(R.id.tvOutTotalPayable);
        tvOutTotalZakat = (TextView) findViewById(R.id.tvOutTotalZakat);
        btnReset = (Button) findViewById(R.id.btnReset);
        infoGoldRate = (ImageView) findViewById(R.id.infoGoldRate);

        btnCalculate.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        infoGoldRate.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            super.onBackPressed();
        }

        return true;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnCalculate)
        {
            if (isInputValid())
            {
                // Perform the calculation
                calculateZakat();
            }
        }
        else if (v.getId() == R.id.btnReset)
        {
            // Reset input fields and output TextViews
            resetFields();
        }
        else if (v.getId() == R.id.infoGoldRate)
        {
            // Handle the click event here
            // For example, you can open a link in a web browser
            Uri uri = Uri.parse("https://www.goodreturns.in/gold-rates/malaysia.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void resetFields()
    {
        // Uncheck RadioButtons
        rbTypeKeep.setChecked(true);
        rbTypeKeep.setChecked(false);

        rbTypeWear.setChecked(true);
        rbTypeWear.setChecked(false);

        // Clear input fields
        GoldWeight.getText().clear();
        CurrentValue.getText().clear();

        // Reset output TextViews to default state
        tvOutTotalGold.setText("0.00");
        tvOutTotalPayable.setText("0.00");
        tvOutTotalZakat.setText("0.00");
    }

    private boolean isInputValid()
    {
        if (GoldWeight.getText().toString().isEmpty() || CurrentValue.getText().toString().isEmpty() || (!rbTypeKeep.isChecked() && !rbTypeWear.isChecked()))
        {
            // If GoldWeight is empty, show a popup message
            showMessage("Please input Gold Weight, Type Gold and Current Value !");
            return false;
        }

        // You can add more validation checks as needed
        return true;
    }

    private void showMessage(String message)
    {
        // Create a TextView with the specified message and text color
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setBackgroundColor(Color.parseColor("#e74c3c"));
        textView.setPadding(20, 20, 20, 20);

        // Set the font size of the TextView
        float textSizeInSp = 20; // Adjust this value as needed
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);

        // Set the width and height of the TextView
        int sizeInDp = 100;
        int sizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp, getResources().getDisplayMetrics());
        textView.setWidth(sizeInPx);
        textView.setHeight(sizeInPx);

        // Center the message in the TextView
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        // Create an AlertDialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(textView)  // Use setView to set the custom view
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();

        // Set the background color of the dialog window
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c3e50")));

        // Center the TextView within the AlertDialog window
        dialog.getWindow().setGravity(Gravity.CENTER);

        // Set the color of the "OK" button text to white
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setTextColor(Color.WHITE);
            }
        });

        dialog.show();
    }

    private void calculateZakat() {
        try {
            // Parse the values from the EditText fields
            double weight = Double.parseDouble(GoldWeight.getText().toString());
            double value = Double.parseDouble(CurrentValue.getText().toString());
            double resultTotalValueGold;
            double resultZakatPayable;
            double resultZakat;

            if (rbTypeKeep.isChecked()) {
                resultTotalValueGold = weight * value;
                resultZakatPayable = Math.max((weight - 85) * value, 0);
                resultZakat = resultZakatPayable * 0.025;
            } else {
                resultTotalValueGold = weight * value;
                resultZakatPayable = Math.max((weight - 200) * value, 0);
                resultZakat = resultZakatPayable * 0.025;
            }

            // Format the results to display with two decimal places
            String formattedTotalValueGold = String.format("%.2f", resultTotalValueGold);
            String formattedZakatPayable = String.format("%.2f", resultZakatPayable);
            String formattedZakat = String.format("%.2f", resultZakat);

            // Set the formatted results to TextViews
            tvOutTotalGold.setText(formattedTotalValueGold);
            tvOutTotalPayable.setText(formattedZakatPayable);
            tvOutTotalZakat.setText(formattedZakat);
        } catch (NumberFormatException e) {
            // Handle the case where the input cannot be parsed as a double
            showMessage("Please enter valid numeric values for Gold Weight and Current Value.");
        }
    }

}