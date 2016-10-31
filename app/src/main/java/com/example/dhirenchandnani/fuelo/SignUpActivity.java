package com.example.dhirenchandnani.fuelo;

import android.content.ClipData;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Dhiren Chandnani on 29-10-2016.
 */

public class SignUpActivity extends AppCompatActivity {
    LinearLayout ll1,ll2,ll3,ll4,ll5,llm;
    LinearLayout mainll;
    LinearLayout.LayoutParams layoutParams;
    static int c = 1;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_form);
        ll1=(LinearLayout) findViewById(R.id.Car_Details_1);
        ll2=(LinearLayout) findViewById(R.id.Car_Details_2);
        ll3=(LinearLayout) findViewById(R.id.Car_Details_3);
        ll4=(LinearLayout) findViewById(R.id.Car_Details_4);
        ll5=(LinearLayout) findViewById(R.id.Car_Details_5);
        llm=ll3;
        layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView myTextView2=(TextView)findViewById(R.id.label_points);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
        myTextView2.setTypeface(typeFace);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();




    }
    public void addCar(View view){

        if(c==5)
            return;
        if(c==1)
            ll2.setVisibility(LinearLayout.VISIBLE);
        if(c==2)
            ll3.setVisibility(LinearLayout.VISIBLE);
        if(c==3)
            ll4.setVisibility(LinearLayout.VISIBLE);
        if(c==4)
            ll5.setVisibility(LinearLayout.VISIBLE);
        c++;
        final ScrollView scrollView = (ScrollView) this.findViewById(R.id.scrollX);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void removeCar(View view){

        if(c==1)
            return;

        if(c==2) {
            ll2.setVisibility(LinearLayout.INVISIBLE);
            EditText k1 = (EditText) findViewById(R.id.ed2a);
            k1.setText("");
            EditText k2 = (EditText) findViewById(R.id.ed2b);
            k2.setText("");
            llm=ll1;


        }
        if(c==3) {
            ll3.setVisibility(LinearLayout.INVISIBLE);
            EditText k1 = (EditText) findViewById(R.id.ed3a);
            k1.setText("");
            EditText k2 = (EditText) findViewById(R.id.ed3b);
            k2.setText("");
            llm=ll2;
        }
        if(c==4) {
            ll4.setVisibility(LinearLayout.INVISIBLE);
            EditText k1 = (EditText) findViewById(R.id.ed4a);
            k1.setText("");
            EditText k2 = (EditText) findViewById(R.id.ed4b);
            k2.setText("");
            llm=ll3;
        }
            if(c==5) {

                ll5.setVisibility(LinearLayout.INVISIBLE);
                EditText k1 = (EditText) findViewById(R.id.ed5a);
                k1.setText("");
                EditText k2 = (EditText) findViewById(R.id.ed5b);
                k2.setText("");
                llm=ll4;
            }
                c--;



        final ScrollView scrollView = (ScrollView) this.findViewById(R.id.scrollX);
        scrollView.post(new Runnable() {
            @Override
            public void run() {

                scrollView.scrollTo(0, llm.getBottom());
            }
        });
    }

    public void registerUser(View view){
        String cars_list_name="";
        String cars_list_type="";
        EditText et1,et2;
        for(int i=0;i<c;i++){
            int ed1id = getResources().getIdentifier("ed"+(i+1)+"a", "id", getPackageName());
            int ed2id = getResources().getIdentifier("ed"+(i+1)+"b", "id", getPackageName());
            et1 = (EditText) findViewById(ed1id);
            et2 = (EditText) findViewById(ed2id);
            cars_list_name+= et1.getText().toString()+"///";
            cars_list_type+= et2.getText().toString()+"///";

        }
        EditText name = (EditText) findViewById(R.id.name_reg);
        String name_ = name.getText().toString();

        EditText email = (EditText) findViewById(R.id.email_reg);
        String email_ = email.getText().toString();

        EditText password = (EditText) findViewById(R.id.password_reg);
        String password_ = password.getText().toString();

        EditText reppass = (EditText) findViewById(R.id.reppassword_reg);
        String reppass_ = reppass.getText().toString();

        if(password_.equals(reppass_)) {

            new RegisterUserActivity(this).execute(c+"", cars_list_name, cars_list_type, name_, password_, email_);
        }
        else{
            Toast toast= Toast.makeText(this, "Passwords Don't Match!", Toast.LENGTH_SHORT);
            toast.setMargin(150,150);
            toast.show();

        }


    }


}
