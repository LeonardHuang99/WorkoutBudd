package com.indestructibles.workoutbudd.Videos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.indestructibles.workoutbudd.R;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mListView = findViewById(R.id.videoActivity);

        ArrayList<Videos> arrayList = new ArrayList<>();

        arrayList.add(new Videos(R.drawable.item1,"15min débutant sport complet à la maison !","Tibo Inshape","https://www.youtube.com/watch?v=0yT5DsGZErc"));
        arrayList.add(new Videos(R.drawable.item2,"BRÛLE-GRAISSES & CALORIES INTENSE !!!","Sissy MUA","https://www.youtube.com/watch?v=GBuOjdKPEI0"));
        arrayList.add(new Videos(R.drawable.item3,"Total ABS & Full Body Workout 20 Min","Chloe Ting","https://www.youtube.com/watch?v=kl5AhFQtfvg"));
        arrayList.add(new Videos(R.drawable.item4,"15Min. Full Body Stretch","Mady Morrison","https://www.youtube.com/watch?v=g_tea8ZNk5A"));
        arrayList.add(new Videos(R.drawable.item5,"30 MIN CALORIE KILLER HIT Workout","growingannanas","https://www.youtube.com/watch?v=jpizoUy4K9s"));
        arrayList.add(new Videos(R.drawable.item6,"12 Min HAPPY CARDIO","Pamela Reif","https://www.youtube.com/watch?v=QPKXw8XEQiA"));

        VideosAdapter videosAdapter = new VideosAdapter(this, R.layout.list_videos, arrayList);

        mListView.setAdapter(videosAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Videos currentVideo = videosAdapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentVideo.getLink()));
                startActivity(i);
            }
        });
    }
}