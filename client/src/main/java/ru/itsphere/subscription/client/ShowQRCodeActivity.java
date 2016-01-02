package ru.itsphere.subscription.client;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import ru.itsphere.subscription.client.qrcode.Contents;
import ru.itsphere.subscription.client.qrcode.QRCodeEncoder;
import ru.itsphere.subscription.domain.Client;

public class ShowQRCodeActivity extends AppCompatActivity {

    private static final String tag = ShowQRCodeActivity.class.getName();
    private ClientApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);
        context = (ClientApplication) this.getApplicationContext();
        showQRCode();
    }

    private void showQRCode() {
        Client client = context.getCurrentClient();
        String data = new Gson().toJson(client);
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(data,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                findScreenSize());
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.qrcode_image);
            myImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e(tag, "QR code generating error:", e);
            Toast.makeText(getApplicationContext(),
                    getString(R.string.qr_error_generate_qr_code), Toast.LENGTH_LONG).show();
        }
    }

    private int findScreenSize() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        return smallerDimension;
    }
}
