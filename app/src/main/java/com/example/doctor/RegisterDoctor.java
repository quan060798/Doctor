package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterDoctor extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView dProfile;
    private EditText drName, drEmailAddress, drSpecialist,drLocation, drPhone, drPassword, drFees, drExperience;
    private Spinner spinner;
    private Button register;
    private ProgressDialog progressDialog;
    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //permission array
    private String[] cameraPermission;
    private String[] storagePermission;
    //image picked uri
    private Uri image_uri;
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;
    String name, email, specialist, location, phone, password, fees, experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        dProfile = findViewById(R.id.drProfile);
        drName = findViewById(R.id.ed_drname);
        drEmailAddress = findViewById(R.id.ed_emailAddress);
        drSpecialist = findViewById(R.id.ed_specialist);
        drLocation = findViewById(R.id.ed_location);
        drPhone = findViewById(R.id.ed_phone);
        drPassword = findViewById(R.id.ed_password);
        spinner = findViewById(R.id.spinner_specialist);
        register = findViewById(R.id.btn_registerdr);
        drFees = findViewById(R.id.ed_fees);
        drExperience = findViewById(R.id.ed_experience);



        //Array adapter
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.specialist, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Specialist")){

                }
                else{
                    drSpecialist.setEnabled(true);
                    drSpecialist.setText(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        dProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData(){
        name = drName.getText().toString().trim();
        email = drEmailAddress.getText().toString().trim();
        specialist = drSpecialist.getText().toString().trim();
        location = drLocation.getText().toString().trim();
        phone = drPhone.getText().toString().trim();
        password = drPassword.getText().toString().trim();
        fees = drFees.getText().toString().trim();
        experience = drExperience.getText().toString().trim();

        if(drName.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Name is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(),"Invalid Email Address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(drLocation.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Location is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(drPhone.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Phone is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(drPassword.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Password is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(drFees.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Fees is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(drExperience.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Experience is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        createAccount();
    }

    private void createAccount(){
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        //create account and save into firebase
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //account created
                saveAccount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed to create account
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAccount(){
        progressDialog.setMessage("Saving Account Info...");

        if (image_uri==null){
            //save into firebase without image

            //set data to save
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+mAuth.getUid());
            hashMap.put("userType","Doctor");
            hashMap.put("name",""+name);
            hashMap.put("email",""+email);
            hashMap.put("specialist",""+specialist);
            hashMap.put("location",""+location);
            hashMap.put("phone",""+phone);
            hashMap.put("password",""+password);
            hashMap.put("image","");
            hashMap.put("fees",""+fees);
            hashMap.put("experience",""+experience);

            //save to database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(mAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent intentToMain = new Intent(RegisterDoctor.this,MainActivity.class);
                    startActivity(intentToMain);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent intentToMain = new Intent(RegisterDoctor.this,MainActivity.class);
                    startActivity(intentToMain);
                    finish();
                }
            });
        }
        else {
            //save into firebase with image
            String filePathAndName = "profile_images/" + ""+mAuth.getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get uri of uploaded image
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()){
                        //set data to save
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("uid",""+mAuth.getUid());
                        hashMap.put("userType","Doctor");
                        hashMap.put("name",""+name);
                        hashMap.put("email",""+email);
                        hashMap.put("specialist",""+specialist);
                        hashMap.put("location",""+location);
                        hashMap.put("phone",""+phone);
                        hashMap.put("password",""+password);
                        hashMap.put("image",""+downloadImageUri); //uri of uploaded image
                        hashMap.put("fees",""+fees);
                        hashMap.put("experience",""+experience);

                        //save to database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(mAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                FirebaseAuth.getInstance().signOut();
                                Intent intentToMain = new Intent(RegisterDoctor.this,MainActivity.class);
                                startActivity(intentToMain);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                FirebaseAuth.getInstance().signOut();
                                Intent intentToMain = new Intent(RegisterDoctor.this,MainActivity.class);
                                startActivity(intentToMain);
                                finish();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImageDialog() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which==0){
                    //camera clicked
                    if (checkCameraPermission()){
                        //allowed, open camera
                        pickFromCamera();
                    }
                    else {
                        requestCameraPermission();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermission()){
                        //allowed, open gallery
                        pickFromGallery();
                    }
                    else
                        requestStoragePermission();
                }
            }
        }).show();
    }

    private void pickFromGallery() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Camera permissions are necessary...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Storage permissions are necessary...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //get chosen image
                image_uri = data.getData();
                //set to image view
                dProfile.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                dProfile.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String specialist = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}