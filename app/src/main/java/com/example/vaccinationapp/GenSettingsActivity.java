package com.example.vaccinationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;

public class GenSettingsActivity extends AppCompatActivity {

    TextView mTextTitle;
    ImageView mImageBack;
    PrefManager manager;
    TextView mTextAboutTerms;
    LinearLayout mLinearContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_settings);
        manager = new PrefManager(this);

        mInit();
    }

    private void mInit() {
        mTextTitle = findViewById(R.id.title_text);
        mImageBack = findViewById(R.id.back);
        mTextTitle = findViewById(R.id.title_text);
        mTextAboutTerms = findViewById(R.id.about_or_terms);
        mLinearContact = findViewById(R.id.contact_us);

        mImageBack.setOnClickListener(v -> onBackPressed());

        try {
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");

            if (title.equals("About Us")) {
                mTextAboutTerms.setVisibility(View.VISIBLE);
                mLinearContact.setVisibility(View.GONE);
                mTextAboutTerms.setText("Rand Fishkin once tweeted, “Best way to sell something—don’t sell anything. Earn the awareness, respect, and trust of those who might buy”.\n" +
                        "\n" +
                        "Trust and respect are things that have to be earned. Awareness comes from being top of mind. These should all be the end goals of your marketing strategy, from SEO and content marketing to social media and email.\n" +
                        "\n" +
                        "Your company’s About Us page is another opportunity to tell a story that will help you stick in your customer’s minds. (And as Blue Acorn’s study proves, it’s also an opportunity for a sale.)");
            } else if (title.equals("Contact Us")) {
                mTextAboutTerms.setVisibility(View.GONE);
                mLinearContact.setVisibility(View.VISIBLE);
            } else {
                mTextAboutTerms.setVisibility(View.VISIBLE);
                mLinearContact.setVisibility(View.GONE);
                mTextAboutTerms.setText("" +
                        "Vaccination App (referred to as “we”, us”) are the authors and publishers of the app Vaccination App and its sub domains, if any, on the world wide web as well as  providers of health care related services to customers with the aid of other software applications, including but not limited to the Applications  used for Hospital Information System, Customers Relationship Management, Appointments Management, Call center / Contact center,  Mobile applications (referred to as “App”).   All such App s, together with Websites visited, accessed or used by users including at Vaccination premises at centers are referred to as “Services”. This App provides the Services, either on its own or in partnership with its agents, affiliates, associates, representatives or other third parties (together referred to as “Partners”)\n" +
                        "\n" +
                        "\u200D");

            }
            mTextTitle.setText(title);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}