package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class WritePost extends AppCompatActivity
{
    ImageButton cameraBtn;
    ImageView pictureView;

    EditText editText_title;
    EditText editText_price;
    EditText editText_contents;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        cameraBtn = (ImageButton) findViewById(R.id.camera_upload_btn);
        pictureView = (ImageView) findViewById(R.id.camera_image);
        editText_title = (EditText) findViewById(R.id.write_post_title);
        editText_price = (EditText) findViewById(R.id.write_post_price);
        editText_contents = (EditText) findViewById(R.id.write_post_contents);

        //cameraBtn.setOnClickListener();
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
        if(R.id.btn_write==item.getItemId())
            return true;
        else
            return super.onOptionsItemSelected(item);
    }
    /*
    public write_Complete()
    {

    }
    */

}