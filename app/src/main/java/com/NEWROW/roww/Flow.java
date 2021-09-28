package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by USER on 22-12-2015.
 */
public class Flow extends Activity {

    Button directory,E_Bulletin,Announcement,Events,Profile,add_member,celebration,add_event,create_group,document,doc_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow);

        directory = (Button)findViewById(R.id.directory);
        E_Bulletin = (Button)findViewById(R.id.E_Bulletin);
        Announcement = (Button)findViewById(R.id.Announcement);
        Events = (Button)findViewById(R.id.Events);
        Profile = (Button)findViewById(R.id.Profile);
        add_member = (Button)findViewById(R.id.add_member);
        celebration = (Button)findViewById(R.id.celebration);
        add_event = (Button)findViewById(R.id.add_event);
        create_group = (Button)findViewById(R.id.create_group);
        document = (Button)findViewById(R.id.document);
        doc_upload = (Button)findViewById(R.id.doc_upload);


        directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,Directory.class);
                startActivity(i);
            }
        });

        E_Bulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,E_Bulletin.class);

                startActivity(i);
            }
        });

        Announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this, Announcement.class);
                startActivity(i);
            }
        });

        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,Events.class);
                startActivity(i);
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,Profile.class);
                startActivity(i);
            }
        });

        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,AddMembers.class);
                startActivity(i);
            }
        });

        celebration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,Celebration.class);
                startActivity(i);
            }
        });
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this,AddEvent.class);
                startActivity(i);
            }
        });

        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(Flow.this,CreateGroup.class);
                startActivity(i);
            }
        });


        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this, Documents.class);
                startActivity(i);
            }
        });

        doc_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Flow.this, Documents_upload.class);
                startActivity(i);
            }
        });


    }
}
