package ca.ggolda.handoff;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainAddActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView uploadedImageView;
    private TextView mPicOrUpView;
    private String pathString;
    private Uri tempUri;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parcel);

        mPicOrUpView = (TextView) findViewById(R.id.takeoraddimage_text);

        uploadedImageView = (ImageView) findViewById(R.id.item_image_imageview);

        ImageView folderImage = (ImageView) findViewById(R.id.folder_image_device);
        folderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        ImageView cameraButton = (ImageView) this.findViewById(R.id.camera_image_device);
        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                takePhoto();
            }
        });

        // Connect to profileButton and send user to MainProfileActivity
        ImageView profileButton = (ImageView) findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAddActivity.this, MainProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

        });

        // Connect to profileButton and send user to MainProfileActivity
        ImageView searchButton = (ImageView) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAddActivity.this, MainSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

        });

        // Connect to profileButton and send user to MainProfileActivity
        ImageView transactionsButton = (ImageView) findViewById(R.id.transactions_button);
        transactionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAddActivity.this, MainTransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

        });

    }


    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // this line added trying to fix camera
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(this)));

        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    // this function added trying to fix camera
    private File getTempFile(Context context) {
        //it will return /sdcard/image.tmp
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final File file = getTempFile(this);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            Uri imageUri = data.getData();

            uploadedImageView.setImageURI(imageUri);
            mPicOrUpView.setVisibility(View.GONE);

            // Get path from image URI
            pathString = getRealPathFromURI(imageUri);
            Log.e("PATHSTRING", pathString);

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            try {

                FileInputStream fileInputStream = new FileInputStream(file);
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inJustDecodeBounds = true;
                //options.inPreferredConfig = Bitmap.Config.RGB_565;
                //options.inDither = true;

                options.inSampleSize = 2;
                Bitmap photoFactory = BitmapFactory.decodeStream(fileInputStream, null, options);

                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));

                uploadedImageView.setImageBitmap(photoFactory);


                mPicOrUpView.setVisibility(View.GONE);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                pathString = getRealPathFromURI(tempUri);
                Log.e("PATHSTRING", pathString);




                uploadedImageView.setImageBitmap(decodeFile(pathString));




            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Connect to addItem button and send user to MainAddActivity
        TextView confirmAddItem = (TextView) findViewById(R.id.confirm_button);
        confirmAddItem.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {

                                                  EditText editName = (EditText) findViewById(R.id.name_field);
                                                  String itemName = editName.getText().toString();

                                                  EditText editDesc = (EditText) findViewById(R.id.description_field_add);
                                                  String itemDesc = editDesc.getText().toString();

                                                  EditText editPrice = (EditText) findViewById(R.id.price_field);
                                                  Double itemPrice = null;
                                                  if (editPrice.length() != 0) {
                                                      itemPrice = Double.valueOf(editPrice.getText().toString());
                                                  } else {
                                                      itemPrice = Double.valueOf(0);
                                                  }


                                                  ItemAdd newItemAdd = new ItemAdd(itemName, itemPrice, pathString, itemDesc);

                                                  Intent intent = new Intent(MainAddActivity.this, MainAddConfirmActivity.class);
                                                  // Send current item to be used for MainAddConfirmActivity
                                                  intent.putExtra("new_item_add", newItemAdd);
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                          | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                  startActivity(intent);

                                                  // Maybe this will clean up?
                                                  finish();

                                              }

                                          }

        );

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Bitmap decodeFile(String imgPath)
    {
        Bitmap b = null;
        int max_size = 1000;
        File f = new File(imgPath);
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > max_size || o.outWidth > max_size)
            {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        }
        catch (Exception e)
        {
        }
        return b;
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (tempUri != null) {
            // TODO: create a release() method
            //tempUri.release();
            tempUri = null;
        }

        if (imageUri != null) {
            // TODO: create a release() method
            //tempUri.release();
            imageUri = null;
        }
        //finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
