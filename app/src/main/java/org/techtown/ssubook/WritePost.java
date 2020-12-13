package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WritePost extends AppCompatActivity
{
    final int GET_GALLARY_IMAGE = 200;
    ImageView pictureView;
    StorageReference imageRef;
    EditText editText_title;
    EditText editText_price;
    EditText editText_contents;
    Uri selectedImageUri;
    RadioGroup radioGroup_underline, radioGroup_write, radioGroup_clean,radioGroup_naming,RadioGroup_discolor;
    RadioButton rB_underline_none,rB_underline_pencil,rB_underline_pen, rB_write_none,rB_write_pencil,rB_write_pen,rB_clean_true,rB_clean_false,rB_naming_true,rB_naming_false,rB_discolor_true,rB_discolor_false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        pictureView = (ImageView) findViewById(R.id.camera_upload);
        editText_title = (EditText) findViewById(R.id.write_post_title);
        editText_price = (EditText) findViewById(R.id.write_post_price);
        editText_contents = (EditText) findViewById(R.id.write_post_contents);
        rB_underline_none = (RadioButton) findViewById(R.id.radio_underline_none);
        rB_underline_pencil = (RadioButton) findViewById(R.id.radio_underline_pencil);
        rB_underline_pen = (RadioButton) findViewById(R.id.radio_underline_pen);
        rB_write_none = (RadioButton) findViewById(R.id.radio_write_none);
        rB_write_pencil = (RadioButton) findViewById(R.id.radio_write_pencil);
        rB_write_pen = (RadioButton) findViewById(R.id.radio_write_pen);
        rB_clean_true = (RadioButton) findViewById(R.id.radio_clean_true);
        rB_clean_false = (RadioButton) findViewById(R.id.radio_clean_false);
        rB_naming_true = (RadioButton) findViewById(R.id.radio_naming_true);
        rB_naming_false = (RadioButton) findViewById(R.id.radio_naming_false);
        rB_discolor_true = (RadioButton) findViewById(R.id.radio_discolor_true);
        rB_discolor_false = (RadioButton) findViewById(R.id.radio_discolor_false);

        pictureView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLARY_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLARY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            selectedImageUri = data.getData();
            //pictureView.setImageURI(selectedImageUri);
            Glide.with(getApplicationContext()).load(selectedImageUri).into(pictureView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.write_post,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        final FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(R.id.btn_write==item.getItemId())
        {
            //글쓰기 완료,  전송

            if(selectedImageUri!=null)
            {
                //사진 있음. Storage 업로드
                UUID imageUUID = UUID.randomUUID();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                imageRef = storage.getReference().child("Images/"+imageUUID+"-"+selectedImageUri.getLastPathSegment());
                imageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                String title = editText_title.getText().toString();
                                String author = "";
                                if (currentUser != null)
                                    author = currentUser.getUid();
                                //String UID = dataMap.get("UID").toString(); //게시글 UID, deprecated
                                int price = Integer.parseInt(editText_price.getText().toString());

                                String image_URL = uri.toString();
                                final String contents = editText_contents.getText().toString();
                                //책 상태
                                String underbarTrace = "NONE";
                                if (rB_underline_none.isChecked())
                                {
                                    underbarTrace = "NONE";
                                }
                                else if (rB_underline_pencil.isChecked())
                                {
                                    underbarTrace = "PENCIL";
                                }
                                else if (rB_underline_pen.isChecked())
                                {
                                    underbarTrace = "PEN";
                                }

                                String writeTrace = "NONE";
                                if (rB_write_none.isChecked())
                                {
                                    writeTrace = "NONE";
                                }
                                else if (rB_write_pencil.isChecked())
                                {
                                    writeTrace = "PENCIL";
                                }
                                else if (rB_write_pen.isChecked())
                                {
                                    writeTrace = "PEN";
                                }

                                String bookCover = "NONE";
                                if (rB_clean_true.isChecked())
                                {
                                    bookCover = "CLEAN";
                                }
                                else if (rB_clean_false.isChecked())
                                {
                                    bookCover = "DIRTY";
                                }

                                boolean naming = false;
                                if (rB_naming_true.isChecked())
                                {
                                    naming = true;
                                }
                                else if (rB_naming_false.isChecked())
                                {
                                    naming = false;
                                }

                                boolean discolor = false;
                                if (rB_discolor_true.isChecked())
                                {
                                    discolor = true;
                                }
                                else if (rB_discolor_false.isChecked())
                                {
                                    discolor = false;
                                }
                                Map<String, Object> postData = new HashMap<>();
                                postData.put("contents",contents);
                                postData.put("title", title);
                                postData.put("author", author);
                                postData.put("price", price);
                                postData.put("underbarTrace", underbarTrace);
                                postData.put("writeTrace", writeTrace);
                                postData.put("bookCover", bookCover);
                                postData.put("bookCover", bookCover);
                                postData.put("naming", naming);
                                postData.put("discolor", discolor);
                                postData.put("imageURL", image_URL);
                                postData.put("timeStamp", new Date().getTime());
                                firebaseDB.collection("Post").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference)
                                    {
                                        Log.w("WritePost", "Success_with_image");
                                        firebaseDB.collection("Post").document(documentReference.getId()).update("UID",documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>()
                                        {
                                            @Override
                                            public void onSuccess(Void aVoid)
                                            {
                                                Toast.makeText(WritePost.this, "게시글이 정상적으로 작성되었습니다.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(WritePost.this, Feed.class);
                                                startActivity(intent);

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Log.w("WritePost", "Failed", e);
                                        //Todo : Post 실패 시 Toast
                                    }
                                });

                            }
                        });
                    }
                });

            }
            else
            {
                UUID imageUUID = UUID.randomUUID();
                FirebaseStorage storage = FirebaseStorage.getInstance();

                String title = editText_title.getText().toString();
                String author = "";
                if (currentUser != null)
                    author = currentUser.getUid();
                //String UID = dataMap.get("UID").toString(); //게시글 UID, deprecated
                int price = Integer.parseInt(editText_price.getText().toString());

                String image_URL = "";
                final String contents = editText_contents.getText().toString();
                //책 상태
                String underbarTrace = "NONE";
                if (rB_underline_none.isChecked())
                {
                    underbarTrace = "NONE";
                }
                else if (rB_underline_pencil.isChecked())
                {
                    underbarTrace = "PENCIL";
                }
                else if (rB_underline_pen.isChecked())
                {
                    underbarTrace = "PEN";
                }

                String writeTrace = "NONE";
                if (rB_write_none.isChecked())
                {
                    writeTrace = "NONE";
                }
                else if (rB_write_pencil.isChecked())
                {
                    writeTrace = "PENCIL";
                }
                else if (rB_write_pen.isChecked())
                {
                    writeTrace = "PEN";
                }

                String bookCover = "NONE";
                if (rB_clean_true.isChecked())
                {
                    bookCover = "CLEAN";
                }
                else if (rB_clean_false.isChecked())
                {
                    bookCover = "DIRTY";
                }

                boolean naming = false;
                if (rB_naming_true.isChecked())
                {
                    naming = true;
                }
                else if (rB_naming_false.isChecked())
                {
                    naming = false;
                }

                boolean discolor = false;
                if (rB_discolor_true.isChecked())
                {
                    discolor = true;
                }
                else if (rB_discolor_false.isChecked())
                {
                    discolor = false;
                }
                Map<String, Object> postData = new HashMap<>();
                postData.put("contents",contents);
                postData.put("title", title);
                postData.put("author", author);
                postData.put("price", price);
                postData.put("underbarTrace", underbarTrace);
                postData.put("writeTrace", writeTrace);
                postData.put("bookCover", bookCover);
                postData.put("bookCover", bookCover);
                postData.put("naming", naming);
                postData.put("discolor", discolor);
                postData.put("imageURL", image_URL);
                postData.put("timeStamp", new Date().getTime());
                firebaseDB.collection("Post").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Log.w("WritePost", "Success_with_image");
                        firebaseDB.collection("Post").document(documentReference.getId()).update("UID",documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(WritePost.this, "게시글이 정상적으로 작성되었습니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(WritePost.this, Feed.class);
                                startActivity(intent);

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.w("WritePost", "Failed", e);
                        //Todo : Post 실패 시 Toast
                    }
                });





            }
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }




}