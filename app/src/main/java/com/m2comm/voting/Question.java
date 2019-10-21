package com.m2comm.voting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.asthma.R;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;


public class Question extends Activity implements View.OnClickListener {

    SharedPreferences prefs;

    private InputMethodManager imm;
    private ImageView btn3_1;
    private EditText et3_1;
    private TextView tv3_1;
    int index = 0;
    String name,hospital;
    boolean isCheck=true;
    ImageView input_point;
    String room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getWindow().setWindowAnimations(0);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        prefs = getSharedPreferences("voting", MODE_PRIVATE);
        name = prefs.getString("id", null);

        hospital = prefs.getString("hospital", null);
        BottomMenu bottommenu = (BottomMenu) findViewById(R.id.bottommenu);
        bottommenu.setActivity(this);
        bottommenu.setSelectNumber(3);
        input_point = (ImageView) findViewById(R.id.input_point);
        input_point.setColorFilter(getResources().getColor(R.color.select_border));

        btn3_1 = (ImageView)findViewById(R.id.btn3_1);
        btn3_1.setOnClickListener(this);

        tv3_1 = (TextView)findViewById(R.id.tv3_1);
        tv3_1.setOnClickListener(this);
        et3_1 = (EditText)findViewById(R.id.et3_1);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
            case R.id.btn3_1:
                imm.hideSoftInputFromWindow(et3_1.getWindowToken(), 0);
                if(isCheck)
                    QuestionSend();
                break;
            case R.id.tv3_1:
                Intent intent = new Intent(this, Lecture.class);
                startActivityForResult(intent,0);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tv3_1.setText(data.getStringExtra("return"));
            room = data.getStringExtra("room");
        }
    }

    private void QuestionSend(){

        String et_question = et3_1.getText().toString();
        String q_name = tv3_1.getText().toString();
        Toast toast;
        if(et_question.length()==0){
            toast = Toast.makeText(this, getString(R.string.quest_no), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if(q_name.length()==0){
            toast = Toast.makeText(this, getString(R.string.lecture_no), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        isCheck = false;
        HttpAsyncTask question = new HttpAsyncTask(Question.this, new HttpInterface() {
            @Override
            public void onResult(String result) {
                if(result.equals("Y"))
                {
                    new AlertDialog.Builder(Question.this)
                            .setTitle("알림")
                            .setMessage(getString(R.string.quest_success))
                            .setPositiveButton(
                                    "확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            isCheck = true;
                                            et3_1.setText("");
                                            tv3_1.setText(getString(R.string.lecture_def));
                                        }
                                    }).show();
                }
                else
                {
                    new AlertDialog.Builder(Question.this)
                            .setTitle("알림")
                            .setMessage(getString(R.string.network_error))
                            .setPositiveButton(
                                    "확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            isCheck = true;
                                        }
                                    }).show();
                }
            }

        });
        question.execute(new HttpParam("url",Global.QUESTION_URL),
                new HttpParam("question",et_question),
                new HttpParam("room",room),
                new HttpParam("mobile","Y"),
                new HttpParam("lecture",q_name),
                new HttpParam("code","allergy2019s"),
                new HttpParam("id",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)),
                new HttpParam("name",prefs.getString("id", null)),
                new HttpParam("job",prefs.getString("office", null)),
                new HttpParam("license",prefs.getString("license", null))
                );
    }
}
