package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.Result;

import hieubd.dto.UserDTO;
import hieubd.mobilefinal.userUI.QRScanDetailActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerViewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView qrCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);
        qrCodeScanner = findViewById(R.id.qrCodeScanner);

    }
    @Override
    public void onResume() {
        super.onResume();
        qrCodeScanner.setResultHandler(this); // Register ourselves as a handler for scan results.
        qrCodeScanner.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        System.out.println(rawResult.getText());
        UserDTO dto = new Gson().fromJson(rawResult.getText(), UserDTO.class);
        Intent intent = new Intent(this, QRScanDetailActivity.class);
        intent.putExtra("DTO", dto);
        startActivityForResult(intent, 5000);

        // If you would like to resume scanning, call this method below:
        qrCodeScanner.resumeCameraPreview((ZXingScannerView.ResultHandler) this);
    }
}
