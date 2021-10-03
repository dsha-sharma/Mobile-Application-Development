package com.example.multinotepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.editTitle);
        editNoteText = findViewById(R.id.editNoteText);

        //The note text is a multi-line text area (EditText) with no size limit. This
        //should have scrolling capability for when notes exceed the size of the
        //activity.
        editNoteText.setMovementMethod(new ScrollingMovementMethod());

        Intent i = getIntent();
        if(i.getExtras() != null){
            editTitle.setText(i.getStringExtra("TITLE"));
            editNoteText.setText(i.getStringExtra("NOTETEXT"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.saveNote){
            Intent toMain = new Intent();
            //Notes with no title are not saved (Toast message informs the user)
            if(editTitle.getText().toString().trim().isEmpty()){
                setResult(-1, toMain);
                finish();
                Toast.makeText(this, "Untitled Note wasn't saved.", Toast.LENGTH_LONG).show();
            }
            //Save Note
            else{
                Intent i = getIntent();
                //Edit An Existing Old Note
                if(i.getExtras() != null){
                    String title = editTitle.getText().toString().trim();
                    String noteText = editNoteText.getText().toString().trim();
                    //No Changes To Existing Old Note
                    if(noteText.equals(i.getStringExtra("NOTETEXT")) && title.equals(i.getStringExtra("TITLE"))){
                        setResult(-1, toMain);
                        finish();
                    }
                    //Changes To Existing Old Note
                    else{
                        toMain.putExtra("TITLE", editTitle.getText().toString());
                        toMain.putExtra("NOTETEXT", editNoteText.getText().toString());
                        setResult(0, toMain);
                        finish();
                    }
                }
                //Make New Note
                else{
                    toMain.putExtra("NEW-TITLE", editTitle.getText().toString());
                    toMain.putExtra("NEW-NOTETEXT", editNoteText.getText().toString());
                    setResult(0, toMain);
                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        //Notes with no title are not saved (Toast message informs the user)
        if(editTitle.getText().toString().trim().isEmpty()){
            Intent toMain = new Intent();
            setResult(-1, toMain);
            Toast.makeText(this, "Untitled Note wasn't saved.", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Note with title
        else{
            Intent intent1 = new Intent();
            Intent intent2 = getIntent();
            //User wants to edit note
            if(intent2.getExtras() != null) {
                String title = editTitle.getText().toString().trim();
                String noteText = editNoteText.getText().toString().trim();
                //User made no edits
                if (noteText.equals(intent2.getStringExtra("NOTETEXT")) && title.equals(intent2.getStringExtra("TITLE"))) {
                    setResult(-1, intent1);
                    finish();
                }
                //Show dialog box if user did make edits
                else {
                    AlertDialog.Builder ADB = new AlertDialog.Builder(this);
                    //User wants to save note
                    ADB.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent toMain = new Intent();
                            Intent i = getIntent();
                            //Editting An Old Note
                            if(i.getExtras() != null){
                                String title = editTitle.getText().toString().trim();
                                String noteText = editNoteText.getText().toString().trim();
                                //No Changes To Old Note
                                if(noteText.equals(i.getStringExtra("NOTETEXT")) && title.equals(i.getStringExtra("TITLE"))){
                                    setResult(-1, toMain);
                                    finish();
                                }
                                //Changes To Old Note
                                else{
                                    toMain.putExtra("TITLE", editTitle.getText().toString().trim());
                                    toMain.putExtra("NOTETEXT", editNoteText.getText().toString().trim());
                                    setResult(0, toMain);
                                    finish();
                                }
                            }
                            //New Note
                            else{
                                toMain.putExtra("NEW-TITLE", editTitle.getText().toString().trim());
                                toMain.putExtra("NEW-NOTETEXT", editNoteText.getText().toString().trim());
                                setResult(0, toMain);
                                finish();
                            }
                        }
                    });

                    ADB.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent toMain = new Intent();
                            setResult(-1, toMain);
                            finish();
                        }
                    });

                    //Dialog Box
                    ADB.setTitle("Note is not saved!");
                    ADB.setMessage("Save '" + editTitle.getText().toString().trim() + "' note?");
                    AlertDialog AD = ADB.create();
                    AD.show();
                }
            }
            //Save New Note
            else{
                //Another alert dialog
                AlertDialog.Builder ADB = new AlertDialog.Builder(this);

                //User wants to save the note
                ADB.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent toMain = new Intent();
                        toMain.putExtra("NEW-TITLE", editTitle.getText().toString().trim());
                        toMain.putExtra("NEW-NOTETEXT", editNoteText.getText().toString().trim());
                        setResult(0, toMain);
                        finish();
                    }
                });

                ADB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent toMain = new Intent();
                        setResult(-1, toMain);
                        finish();
                    }
                });

                //Dialog Box
                ADB.setTitle("Note is not saved!");
                ADB.setMessage("Save '" + editTitle.getText().toString().trim() + "' note?");
                AlertDialog AD = ADB.create();
                AD.show();
            }
        }

    }

}


