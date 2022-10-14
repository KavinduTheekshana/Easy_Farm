package com.example.easyfarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.easyfarm.Models.BaseURL;
import com.example.easyfarm.Models.OutputDetails;
import com.example.easyfarm.Models.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dashboard extends AppCompatActivity {

    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    ImageView imageView;
    Button button;
    String respond;
    String status;
    DatabaseReference reference;
    private int GALLERY = 1, CAMERA = 2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        layout1 = (LinearLayout) findViewById(R.id.capture);
        layout2 = (LinearLayout) findViewById(R.id.getresult);
        layout3 = (LinearLayout) findViewById(R.id.menu);
        button = (Button) findViewById(R.id.save);
        imageView = findViewById(R.id.image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("OutputDetails");
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                String key = reference.push().getKey();
                OutputDetails outputDetails = new OutputDetails(key,respond,status,currentDate,currentTime);
                reference.child(key).setValue(outputDetails);

                Toast.makeText(Dashboard.this, "Saved Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.INVISIBLE);
                showPictureDialog();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ObjectDetection().execute();
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(Dashboard.this,menu.class);
                startActivity(homeIntent);
                finish();
            }
        });
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    photo = saveImage(bitmap);

                    Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

                    imageView.setImageBitmap(bitmap);
                    System.out.println("MACHAN" + bitmap);
//                    photo=saveImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();

                    Toast.makeText(this, "Image Saved Failed", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
//            photo=saveImage(thumbnail);
            System.out.println("MACHAN" + thumbnail);

            Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    private class ObjectDetection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            if (respond != null){
                button.setVisibility(View.VISIBLE);
            }

            if(respond == "0"){

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard.this);
                builder1.setMessage("Health potato. Image is "+status+"");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }else if (respond == "1"){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard.this);
                builder1.setMessage("Early blight. Image is "+status+"");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }else if(respond == "2"){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Dashboard.this);
                builder1.setMessage("Late blight. Image is "+status+"");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else {
                Toast.makeText(Dashboard.this, "Backend Connection Error", Toast.LENGTH_SHORT).show();
            }


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
            String filename = sdf.format(new Date());

            try {
                String path = getApplicationContext().getFilesDir().getPath();
                OutputStream fOut = null;
                File file = new File(path, "MYFile");
                if (!file.exists()) {
                    file.mkdirs();
                }

                File file2 = new File(file, filename + ".png");
                try {
                    fOut = new FileOutputStream(file2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
//                Bitmap test = imageview.obtainBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

                String Url = BaseURL.get_url()+"cnn_classifier";
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", file2.getName(),RequestBody.create(MediaType.parse("multipart/form-data"), file2)
                        )
                        .build();

                System.out.println(requestBody);

                System.out.println("dddddddddddddddddddddddd" + requestBody);

                Request request = new Request.Builder()
                        .url(Url)
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    ResponseBody body = response.body();
                    JSONArray json = new JSONArray(body.string());
                    System.out.println(json);
//                    JSONArray array = new JSONArray(json);
//                    System.out.println("aaaaaaaaaaaaaaaa" + array);

                    for (int xx = 0; xx < json.length(); xx++) {
                        JSONObject joo = json.getJSONObject(xx);

                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                        respond = joo.getString("respond");
                        status = joo.getString("status");
                        System.out.println(respond);
                        System.out.println(status);

                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}