package com.xxx.gogo.view.about_us;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xxx.gogo.BaseToolBarActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AboutUsActivity extends BaseToolBarActivity implements View.OnClickListener{
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        createNormalToolBar(R.string.about_us, this);

        loadData();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bar_back){
            finish();
        }
    }

    private void loadData(){
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                ByteArrayOutputStream outputStream = null;
                String strContent = "";
                try{
                    inputStream = getAssets().open("about_us");
                    byte[] buffer = new byte[4096];
                    outputStream = new ByteArrayOutputStream();
                    int read;
                    while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }

                    strContent = new String(outputStream.toByteArray());

                }catch (Exception e){
                    LogUtil.e("load about_us data error " + e);
                }finally {
                    try {
                        if(inputStream != null){
                            inputStream.close();
                        }
                        if(outputStream != null){
                            outputStream.close();
                        }
                    }catch (Exception e){

                    }
                }

                final String content = strContent;

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        TextView tvContent = (TextView) findViewById(R.id.id_content);

                        tvContent.setText(content);
                    }
                });
            }
        });
    }
}
