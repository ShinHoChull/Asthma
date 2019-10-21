package com.m2comm.voting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.m2comm.asthma.R;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;


public class Question2 extends Activity implements View.OnClickListener {

    private String phoneNumber;

    SharedPreferences prefs;

    private InputMethodManager imm;
    private Button btn3_1;
    private EditText et3_1;
    int index = 0;
    String name="";
    LinearLayout bg;
    boolean isCheck=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);
        getWindow().setWindowAnimations(0);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        String strRes;
        prefs = getSharedPreferences("voting", MODE_PRIVATE);
        name = prefs.getString("id", null);
        phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BottomMenu bottommenu = (BottomMenu) findViewById(R.id.bottommenu);
        bottommenu.setActivity(this);
        bottommenu.setSelectNumber(3);

        btn3_1 = (Button)findViewById(R.id.btn3_1);
        btn3_1.setOnClickListener(this);
        et3_1 = (EditText)findViewById(R.id.et3_1);
        bg = (LinearLayout)findViewById(R.id.bg);
        bg.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
            case R.id.bg:
                imm.hideSoftInputFromWindow(et3_1.getWindowToken(), 0);
                break;
            case R.id.btn3_1:
                imm.hideSoftInputFromWindow(et3_1.getWindowToken(), 0);
                if(isCheck)
                    QuestionSend();
                break;

        }
    }




    private void QuestionSend(){
        String et_question = et3_1.getText().toString();
        Toast toast;
        if(et_question.length()==0){
            toast = Toast.makeText(this, getString(R.string.quest_no), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        isCheck = false;
        HttpAsyncTask question = new HttpAsyncTask(Question2.this, new HttpInterface() {
            @Override
            public void onResult(String result) {
                if(result.equals("Y"))
                {
                    new AlertDialog.Builder(Question2.this)
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
                                            index = 0;
                                        }
                                    }).show();
                }
                else
                {
                    new AlertDialog.Builder(Question2.this)
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
                new HttpParam("lecture",""),
                new HttpParam("code",Global.CODE),
                new HttpParam("id",Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)),
                new HttpParam("name",prefs.getString("id", null)),
                new HttpParam("job",prefs.getString("office", null)),
                new HttpParam("license",prefs.getString("license", null))
        );

     }
}
