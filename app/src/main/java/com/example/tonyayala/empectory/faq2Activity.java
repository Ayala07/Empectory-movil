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

public class faq2Activity extends AppCompatActivity{

    LinearLayout quesrionOne;
    LinearLayout questionTwo;
    LinearLayout questionThree;
    LinearLayout questionFour;
    LinearLayout questionFive;
    LinearLayout questionSix;
    LinearLayout respOne;
    LinearLayout respTwo;
    LinearLayout respThree;
    LinearLayout respFour;
    LinearLayout respFive;
    LinearLayout respSix;
    Button btnClose;
    Animation fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq2);


        quesrionOne = findViewById(R.id.questionOne);
        questionTwo = findViewById(R.id.questionTwo);
        questionThree = findViewById(R.id.questionThree);
        questionFour = findViewById(R.id.questionFour);
        questionFive = findViewById(R.id.questionFive);
        questionSix = findViewById(R.id.questionSix);

        respOne = findViewById(R.id.respOne);
        respTwo = findViewById(R.id.respTwo);
        respThree = findViewById(R.id.respTrhee);
        respFour = findViewById(R.id.respFour);
        respFive = findViewById(R.id.respFive);
        respSix = findViewById(R.id.respSix);


        btnClose = findViewById(R.id.btn_close);

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
                    questionFive.setClickable(false);
                    questionSix.setClickable(false);

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
                    questionFive.setClickable(false);
                    questionSix.setClickable(false);
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
                    questionFive.setClickable(false);
                    questionSix.setClickable(false);
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
                    respFour.setAnimation(fade);

                    quesrionOne.setClickable(false);
                    questionTwo.setClickable(false);
                    questionThree.setClickable(false);
                    questionFive.setClickable(false);
                    questionSix.setClickable(false);
                }

            }
        });

        questionFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    questionFive.setBackground(getDrawable(R.drawable.bg_item_selected));
                    respFive.setVisibility(View.VISIBLE);

                    btnClose.setAlpha(1);
                    respFive.setAnimation(fade);

                    quesrionOne.setClickable(false);
                    questionTwo.setClickable(false);
                    questionThree.setClickable(false);
                    questionFour.setClickable(false);
                    questionSix.setClickable(false);
                }

            }
        });

        questionSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    questionSix.setBackground(getDrawable(R.drawable.bg_item_selected));
                    respSix.setVisibility(View.VISIBLE);

                    btnClose.setAlpha(1);
                    respSix.setAnimation(fade);

                    quesrionOne.setClickable(false);
                    questionTwo.setClickable(false);
                    questionThree.setClickable(false);
                    questionFour.setClickable(false);
                    questionFive.setClickable(false);
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
                    questionFive.setBackground(getDrawable(R.drawable.bg_item));
                    questionSix.setBackground(getDrawable(R.drawable.bg_item));

                    respOne.setVisibility(View.GONE);
                    respTwo.setVisibility(View.GONE);
                    respThree.setVisibility(View.GONE);
                    respFour.setVisibility(View.GONE);
                    respFive.setVisibility(View.GONE);
                    respSix.setVisibility(View.GONE);


                    quesrionOne.setClickable(true);
                    questionTwo.setClickable(true);
                    questionThree.setClickable(true);
                    questionFour.setClickable(true);
                    questionFive.setClickable(true);
                    questionSix.setClickable(true);

                    btnClose.setAlpha(0);

                }

            }
        });
    }
}
