package net.elorrieta.prueba_compartirfoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
	Button buttonLoadImage;
	ImageView imageView;
	Button bCompartirImagen;
	Bitmap oBitmap;
	Context context;
	final int REQUEST_IMAGE_CAPTURE  = 1234;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonLoadImage =   findViewById(R.id.loadImage);
		imageView =         findViewById(R.id.targetImage);
		bCompartirImagen =  findViewById(R.id.botonCompartir);
		context =           this;



	}
	public void compartirImagen(View view) {
		File imagePath =    new File(context.getCacheDir(), "images");
		File newFile =      new File(imagePath, "image.png");
		Uri contentUri =    FileProvider.getUriForFile(context, "net.elorrieta.prueba_compartirfoto.fileprovider", newFile);

		if (contentUri != null) {
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
			shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
			shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
			startActivity(Intent.createChooser(shareIntent, "Elige una app"));
		}
	}
	// Guardar la foto en la cache de la aplicación. CUIDADO: getCacheDir() La cache es temporal.
	public void guardarFoto(){
		try {

			File cachePath =            new File(context.getCacheDir(), "images");
			cachePath.mkdirs();     // Cuidado no olvidarse de crear el directorio.
			FileOutputStream stream =   new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
			oBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void cargarFoto(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		} catch (ActivityNotFoundException e) {
			// display error state to the user
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras =     data.getExtras();
			oBitmap =           (Bitmap) extras.get("data");// Obtener el bitmap.
			imageView.setImageBitmap(oBitmap); // Mostrar la imagen en el ImageView
			guardarFoto();  // Guardarlo en la cache.
		}


	}



}