package com.anandgaur.spingame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anandgaur.spingame.wheel.SpinningWheelView;

import java.util.Random;

public class PlayGameActivity extends AppCompatActivity implements SpinningWheelView.OnRotationListener {

    ImageView btnSpin;
    SpinningWheelView wheelView;
    ImageView imgback;
    TextView tvCoins;
    private Dialog main_dialogue;
    int degree;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        random = new Random();
        degree = random.nextInt(180);

        Log.d("TAG", "onCreate: " + degree);

        btnSpin = findViewById(R.id.buttonSpin);
        wheelView = findViewById(R.id.wheel);
        imgback = findViewById(R.id.img_back);
        tvCoins = findViewById(R.id.tv_coins);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wheelView.setOnRotationListener(this);
        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                wheelView.rotate(degree, 5000, 60);

                degree = random.nextInt(180);

                Log.d("TAG", "degree: " + degree);

            }
        });


    }

    @Override
    public void onRotation() {

    }

    @Override
    public void onStopRotation(final Object item) {

        LayoutInflater dialogue_layout = LayoutInflater.from(PlayGameActivity.this);
        final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout, null);
        main_dialogue = new Dialog(PlayGameActivity.this);
        main_dialogue.setContentView(dialogueview);
        final Button btn_cancel, btn_claim;
        final TextView tv_alert_msg, tv_count;

        btn_cancel = dialogueview.findViewById(R.id.btn_cancel);
        btn_claim = dialogueview.findViewById(R.id.btn_claim);
        btn_cancel.setText("Cancel");
        btn_claim.setText("Claim");

        tv_alert_msg = dialogueview.findViewById(R.id.tv_alert_msg);
        tv_count = dialogueview.findViewById(R.id.tv_count);
        tv_count.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        main_dialogue.getWindow().setAttributes(layoutParams);
        main_dialogue.setCancelable(false);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_dialogue.dismiss();

            }
        });

        btn_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = item.toString();
                amount = amount.substring(1);


                int coins = Integer.parseInt(tvCoins.getText().toString());
                int new_coins = Integer.parseInt(amount);
                tvCoins.setText(String.valueOf(coins + new_coins));


                main_dialogue.dismiss();

            }
        });

        main_dialogue.show();

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_claim.setEnabled(false);  //BUTTON_POSITIVE is False
                tv_count.setText("" + (millisUntilFinished / 1000) + "");
                tv_alert_msg.setText("Please Wait to " + (millisUntilFinished / 1000) + " seconds for add coins into wallet!!");

            }

            @Override
            public void onFinish() {
                btn_claim.setEnabled(true); //BUTTON_POSITIVE is Enable
            }
        }.start();


    }
}