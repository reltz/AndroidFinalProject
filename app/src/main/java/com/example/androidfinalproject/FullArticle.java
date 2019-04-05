package com.example.androidfinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class FullArticle extends AppCompatActivity {
    TextView title;
    TextView body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        title=findViewById(R.id.news_title_detailed);
        body=findViewById(R.id.news_body);

        Article current = new Article("Efforts to Legalize Marijuana in New Jersey Collapses", "TRENTON — A monthslong effort to legalize marijuana in New Jersey collapsed on Monday after Democrats were unable to muster enough support for the measure, rejecting a central campaign pledge from Gov. Philip D. Murphy and leaving the future of the legalization movement in doubt.\n" +
                "\n" +
                "The failure in the legislature marks one of the biggest setbacks for Mr. Murphy, who despite having full Democratic control in the State Senate and the assembly, had faced constant party infighting and had struggled to bend the legislature to his progressive agenda.\n" +
                "\n" +
                "[What you need to know to start the day: Get New York Today", 1);
        title.setText(current.getTitle());
        body.setText(current.getBody());

        Toast.makeText(getApplicationContext(), R.string.nySaveAlert,
                Toast.LENGTH_LONG).show();
    }
}
