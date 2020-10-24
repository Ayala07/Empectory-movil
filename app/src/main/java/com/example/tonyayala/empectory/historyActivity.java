package com.example.tonyayala.empectory;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class historyActivity extends AppCompatActivity {

    LinearLayout quesrionOne;
    LinearLayout questionTwo;
    LinearLayout questionThree;
    LinearLayout questionFour;
    LinearLayout respOne;
    LinearLayout respTwo;
    LinearLayout respThree;
    LinearLayout respFour;
    Button btnClose;
    Animation fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        quesrionOne = findViewById(R.id.questionOneH);
        questionTwo = findViewById(R.id.questionTwoH);
        questionThree = findViewById(R.id.questionThreeH);
        questionFour= findViewById(R.id.questionFourH);
        respOne = findViewById(R.id.respOneH);
        respTwo = findViewById(R.id.respTwoH);
        respThree = findViewById(R.id.respTrheeH);
        respFour= findViewById(R.id.respFourH);


        btnClose = findViewById(R.id.btn_closeH);

        btnClose.setAlpha(0);




        fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        quesrionOne.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    quesrionOne.setBackground(getDrawable(R.drawable.bg_item_selected));
                    respOne.setVisibility(View.VISIBLE);

                    btnClose.setAlpha(1);
                    respOne.setAnimation(fade);

                    questionTwo.setClickable(false);
                    questionThree.setClickable(false);
                    questionFour.setClickable(false);

                }
            }
        });

        questionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    questionTwo.setBackground(getDrawable(R.drawable.bg_item_selected));
                    respTwo.setVisibility(View.VISIBLE);

                    btnClose.setAlpha(1);
                    respTwo.setAnimation(fade);

                    quesrionOne.setClickable(false);
                    questionThree.setClickable(false);
                    questionFour.setClickable(false);
                }

            }
        });

        questionThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    questionThree.setBackground(getDrawable(R.drawable.bg_item_selected));
                    respThree.setVisibility(View.VISIBLE);

                    btnClose.setAlpha(1);
                    respThree.setAnimation(fade);

                    quesrionOne.setClickable(false);
                    questionTwo.setClickable(false);
                    questionFour.setClickable(false);
                }

            }
        });

        questionFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    questionFour.setBackground(getDrawable(R.drawable.bg_item_selected));
                    respFour.setVisibility(View.VISIBLE);

                    btnClose.setAlpha(1);
                    respTwo.setAnimation(fade);

                    quesrionOne.setClickable(false);
                    questionTwo.setClickable(false);
                    questionThree.setClickable(false);
                }

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    quesrionOne.setBackground(getDrawable(R.drawable.bg_item));
                    questionTwo.setBackground(getDrawable(R.drawable.bg_item));
                    questionThree.setBackground(getDrawable(R.drawable.bg_item));
                    questionFour.setBackground(getDrawable(R.drawable.bg_item));

                    respOne.setVisibility(View.GONE);
                    respTwo.setVisibility(View.GONE);
                    respThree.setVisibility(View.GONE);
                    respFour.setVisibility(View.GONE);


                    quesrionOne.setClickable(true);
                    questionTwo.setClickable(true);
                    questionThree.setClickable(true);
                    questionFour.setClickable(true);

                    btnClose.setAlpha(0);

                }

            }
        });
    }
}
