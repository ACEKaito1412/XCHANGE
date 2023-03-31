package com.project.xchange.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.xchange.R;
import com.project.xchange.send_confirm;
import com.project.xchange.userHandler;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qr_pay_payment extends AppCompatActivity {
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;
    private ImageView qrCodeIV;
    private String DATAID, DATANAME;

    private Button btn_continue;

    private TextView nameView;
    private EditText paymentAmount;

    private userHandler userhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_pay_payment);

        qrCodeIV = findViewById(R.id.qrView);

        DATAID = getIntent().getStringExtra("DATAID");
        DATANAME = getIntent().getStringExtra("DATANAME");

        paymentAmount = findViewById(R.id.x_payment_amount);

        nameView = (TextView) findViewById(R.id.x_changeName);
        nameView.setText(DATANAME);

        userhandler = new userHandler();

        btn_continue = (Button) findViewById(R.id.continue_btn);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = paymentAmount.getText().toString().trim();

                if(amount.isEmpty()){
                    paymentAmount.setError("Enter Amount");
                    paymentAmount.requestFocus();
                    return;
                }

                if(Double.valueOf(amount) > userhandler.getUserBalance()){
                    paymentAmount.setError("Account Doesn't have Enough balance ");
                    Toast.makeText(qr_pay_payment.this, "INVALID REQUEST", Toast.LENGTH_SHORT).show();
                    paymentAmount.requestFocus();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), send_confirm.class);
                intent.putExtra("addressId", DATAID);
                intent.putExtra("sendAmount", amount);
                intent.putExtra("billAddress", "");
                intent.putExtra("billType", "");
                startActivity(intent);
            }
        });

        createQR(DATAID, qrCodeIV, bitmap);
    }

    private void createQR(String data, ImageView idView, Bitmap bitmap){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, dimen);
        qrgEncoder.setColorBlack(Color.RED);
        qrgEncoder.setColorWhite(Color.argb(1, 2,2,2));
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            qrCodeIV.setImageBitmap(bitmap);
        } catch (Exception e) {
            // this method is called for
            // exception handling.
        }
    }
}