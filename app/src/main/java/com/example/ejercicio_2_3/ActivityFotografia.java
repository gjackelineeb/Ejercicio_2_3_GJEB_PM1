package com.example.ejercicio_2_3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ejercicio_2_3.photos.SQLiteConexion;
import com.example.ejercicio_2_3.photos.Transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityFotografia extends AppCompatActivity {

    Button btnGuardarAF,btnGaleriaAF;
    FloatingActionButton btnFotoAF;
    ImageView foto;
    EditText txtDescriptionAF;
    Bitmap imagen;

    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        btnGuardarAF = (Button) findViewById(R.id.btnGuardarAF);
        btnGaleriaAF = (Button) findViewById(R.id.btnGaleriaAF);
        btnFotoAF = (FloatingActionButton) findViewById(R.id.btnTomarFoto);
        foto = (ImageView) findViewById(R.id.foto);
        txtDescriptionAF = (EditText) findViewById(R.id.txtDescription);

        btnFotoAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtorgarPermisos();
            }
        });

        btnGuardarAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GuardarDatos();
            }
        });

        btnGaleriaAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Listado.class);
                startActivity(intent);
            }
        });


    }

    private void GuardarDatos() {
        try {
            Foto(imagen);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            Intent intent = new Intent(this, ActivityFotografia.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            LimpiarTextos();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Tome una Fotografia!",Toast.LENGTH_LONG).show();
        }


    }

    private void Foto( Bitmap bitmap) {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.descripcion,txtDescriptionAF.getText().toString());
        valores.put(String.valueOf(Transacciones.foto),ArrayFoto);

        Long resultado = db.insert(Transacciones.tblFotografias, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro Completado. Foto N° " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }

    private void LimpiarTextos() {
        txtDescriptionAF.setText("");
    }

    private void OtorgarPermisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            FomarFoto();
        }
    }

    private void FomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                FomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Permita el acceso a la camara",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Byte [] arreglo;

        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            foto.setImageBitmap(imagen);
        }

    }

}