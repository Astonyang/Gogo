package com.xxx.gogo.view.about_us;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;

public class AboutUsActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        createNormalToolBar(R.string.about_us, this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }
}
