package com.NEWROW.row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.SimpleImageGalleryAdapter;
import com.NEWROW.row.Adapter.SimpleMediaAdapter;
import com.NEWROW.row.Data.SimpleGalleryItemData;
import com.NEWROW.row.Data.SimplePhotoData;
import com.NEWROW.row.Data.UploadPhotoData;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.services.UploadPhotoService;
import com.NEWROW.row.sql.UploadedPhotoModel;

import java.util.ArrayList;

/**
 * Created by user on 03-11-2016.
 */
public class AddPhotoActivity extends Activity implements SimpleMediaAdapter.OnMediaSelectListener,SimpleMediaAdapter.OnMediaRemoveListener {
    ViewPager imageViewPager;
    EditText etImageDescription;
    RecyclerView rvGallery;
    TextView btnDone;
    Context context;
    SimpleMediaAdapter adapter;
    SimpleImageGalleryAdapter pagerAdapter;
    static final int PHOTO_SELECTION_REQUEST = 10;
    ArrayList<Object> imageList;
    UploadedPhotoModel addPhotoModel;
    UploadPhotoData data;
    String albumId = "";
    String groupId = "";
    String createdBy = "";
    int count=0;
    ArrayList<SimplePhotoData> selectedPhotos;
    int selectedPosition = 0;
    TextView tv_title;
    ImageView iv_actionbtn;
    ImageView btnPrevious, btnNext;
    Button btn_loadmore;
    RecyclerView.ViewHolder view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        btnPrevious = (ImageView) findViewById(R.id.btnPrevious);
        btnNext = (ImageView) findViewById(R.id.btnNext);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setImageResource(R.drawable.delete);
        btn_loadmore = (Button)findViewById(R.id.btn_loadmore);
        iv_actionbtn.setVisibility(View.VISIBLE);

        tv_title.setText("Add Photo");
        Intent i = getIntent();
        albumId = i.getStringExtra("albumId");
        count = i.getExtras().getInt("count");

        groupId= PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID);
        createdBy= PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID);

        try {

            context = this;

            imageViewPager = (ViewPager) findViewById(R.id.photoPager);
            etImageDescription = (EditText) findViewById(R.id.etImageDescription);
            rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
            btnDone = (TextView) findViewById(R.id.btnDone);

            addPhotoModel = new UploadedPhotoModel(this);

            imageList = new ArrayList<Object>();

            adapter = new SimpleMediaAdapter(context, imageList);
            adapter.setOnMediaSelectListener(this);
            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvGallery.setLayoutManager(manager);

            rvGallery.setAdapter(adapter);

            pagerAdapter = new SimpleImageGalleryAdapter(context, imageList);

            imageViewPager.setAdapter(pagerAdapter);

            if ( selectedPosition == 0 ) {
                btnPrevious.setVisibility(View.GONE);
            }

            btnPrevious.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = imageViewPager.getCurrentItem();
                    imageViewPager.setCurrentItem(position-1);
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = imageViewPager.getCurrentItem();
                    imageViewPager.setCurrentItem(position+1);
                }
            });

            init();

            Intent intent = new Intent(context, AlbumFolderPage.class);
            intent.putExtra("selectedIamges", imageList);
            startActivityForResult(intent, PHOTO_SELECTION_REQUEST);


        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "We are sorry for inconvenience. Something went wrong.", Toast.LENGTH_LONG).show();
        }

    }

    public void init() {

        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                view= rvGallery.findViewHolderForAdapterPosition(position);
                adapter.color(view);
                //etImageDescription.setText(((SimplePhotoData) spd).getDescription());
                if ( imageList.get(position) instanceof SimpleGalleryItemData ) {
                    SimpleGalleryItemData sd = (SimpleGalleryItemData) imageList.get(position);
                    etImageDescription.setText(sd.getDescription());
                }

                if (selectedPosition == 0 ) {
                    btnPrevious.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                } else if ( position == imageList.size() - 1 ) {
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                }
                else {
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                hideKeyboard();

                if((imageList.size()+count)>5){
                    Toast.makeText(AddPhotoActivity.this,"Maximum 5 photos are allowed.",Toast.LENGTH_SHORT).show();
                    return;
                }

             for(int i = 0;i< imageList.size();i++) {

                 data = new UploadPhotoData("0",selectedPhotos.get(i).getUrl(),selectedPhotos.get(i).getDescription(),albumId,groupId,createdBy,"0");
                 long id= addPhotoModel.insert(data);

                 if(id>0 && imageList.size()-1 == i){

                  //   Toast.makeText(AddPhotoActivity.this,"Data saved Successfully",Toast.LENGTH_SHORT).show();
                 }
             }


                finish();

                Log.d("UploadPhotoService", "UploadPhotoService is Called");

                Intent intent = new Intent(AddPhotoActivity.this, UploadPhotoService.class);
                startService(intent);

            }
        });


        /*etImageDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                selectedPhotos.get(selectedPosition).setDescription(etImageDescription.getText().toString());
                Log.e("Touchbase", "♦♦♦♦Selected photo details : " + selectedPhotos.get(selectedPosition).toString());
                return false;
            }
        });*/

        etImageDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((SimpleGalleryItemData)imageList.get(selectedPosition)).setDescription(etImageDescription.getText().toString());
                Log.e("Touchbase", "♦♦♦♦Selected photo details : " + ((SimpleGalleryItemData)imageList.get(selectedPosition)).toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPhotos.remove(selectedPosition);
                imageList.remove(selectedPosition);
                adapter.notifyDataSetChanged();
                pagerAdapter.notifyDataSetChanged();
                    if(imageList.size() >0) {
                        imageViewPager.setCurrentItem(selectedPosition);

                }else{
                        Intent intent = new Intent(context, AlbumFolderPage.class);
                        startActivityForResult(intent, PHOTO_SELECTION_REQUEST);
                }
            }
        });

        btn_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context, AlbumFolderPage.class);
//                intent.putExtra("selectedIamges", imageList);
//                startActivityForResult(intent, PHOTO_SELECTION_REQUEST);

                Intent intent = new Intent(context, PicturesinsideAlbum.class);
                intent.putExtra("Foldername",PreferenceManager.getPreference(AddPhotoActivity.this,"galleryfoldername"));
                intent.putExtra("isloadmore","yes");
                intent.putExtra("Existing photo list",selectedPhotos);
                startActivityForResult(intent, PHOTO_SELECTION_REQUEST);

                imageList.clear();
                adapter.notifyDataSetChanged();
                pagerAdapter.notifyDataSetChanged();



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if ( requestCode == PHOTO_SELECTION_REQUEST ) {
                selectedPhotos = data.getParcelableArrayListExtra("selectedPhotos");
                imageList.addAll(selectedPhotos);
                adapter.notifyDataSetChanged();
                pagerAdapter.notifyDataSetChanged();
                System.out.println("Touchbase : Selected photos list is : "+imageList);
            }
        }else{
            finish();
        }
    }

    @Override
    public void onMediaSelected(SimpleGalleryItemData spd, int position) {
        if (spd instanceof SimplePhotoData) {

            selectedPosition = position;

            etImageDescription.setText(((SimplePhotoData) spd).getDescription());
            imageViewPager.setCurrentItem(position);
        }
    }

    @Override
    public void onMediaDeleted(SimpleGalleryItemData spd, int position) {
        if (spd instanceof SimplePhotoData) {

            selectedPosition = position;

            imageViewPager.removeViewAt(position);
        }
    }


    public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
