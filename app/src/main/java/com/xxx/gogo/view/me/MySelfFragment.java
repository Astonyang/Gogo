package com.xxx.gogo.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.xxx.gogo.BusEvent;
import com.xxx.gogo.MainActivity;
import com.xxx.gogo.R;
import com.xxx.gogo.manager.BusFactory;
import com.xxx.gogo.manager.user.UserEvent;
import com.xxx.gogo.manager.user.UserManager;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.StartupMetrics;
import com.xxx.gogo.utils.ToastManager;
import com.xxx.gogo.view.about_us.AboutUsActivity;
import com.xxx.gogo.view.order.OrderListActivity;
import com.xxx.gogo.view.shop_mgr.ShopManagerActivity;
import com.xxx.gogo.view.user.LoginActivity;
import com.xxx.gogo.view.user.UserInfoActivity;

public class MySelfFragment extends Fragment implements View.OnClickListener{
    private TextView mBadgeView;
    private View mLoginView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusFactory.getBus().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StartupMetrics.Log("MySelfFragment::onCreateView");

        ScrollView root = (ScrollView) inflater.inflate(R.layout.me, container, false);

        (mLoginView = root.findViewById(R.id.login_btn)).setOnClickListener(this);
        root.findViewById(R.id.order).setOnClickListener(this);
        root.findViewById(R.id.user_profile).setOnClickListener(this);
        root.findViewById(R.id.restaurant_manager).setOnClickListener(this);
        root.findViewById(R.id.setting).setOnClickListener(this);
        root.findViewById(R.id.about_us).setOnClickListener(this);
        root.findViewById(R.id.update).setOnClickListener(this);

        if(UserManager.getInstance().isLogin()){
            mLoginView.setVisibility(View.GONE);
        }else {
            mLoginView.setVisibility(View.VISIBLE);
        }

        mBadgeView = (TextView) root.findViewById(R.id.id_badge);

        testBadge();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusFactory.getBus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.order){
            Intent intent = new Intent(getContext(), OrderListActivity.class);
            startActivityForResult(intent, Constants.START_ORDER_LIST_ACTIVITY);
        }else if(v.getId() == R.id.login_btn){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.START_LOGIN_FROM_ME);
        }else if(R.id.user_profile == v.getId()){
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            startActivity(intent);
        }else if(R.id.restaurant_manager == v.getId()) {
            Intent intent = new Intent(getContext(), ShopManagerActivity.class);
            startActivity(intent);
        }else if(R.id.setting == v.getId()){
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
        }else if(R.id.about_us == v.getId()){
            Intent intent = new Intent(getContext(), AboutUsActivity.class);
            startActivity(intent);
        }else if(R.id.update == v.getId()){
            ToastManager.showToast(getContext(), getResources().getString(
                    R.string.update_already_is_latest_ver));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.START_ORDER_LIST_ACTIVITY && data != null){
            boolean exit = data.getBooleanExtra(Constants.KEY_SWITCH_SHOP_CART, false);
            if(exit){
                BusFactory.getBus().post(new BusEvent.TabSwitcher(BusEvent.TabSwitcher.TAB_SHOPCART));
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event){
        if(event instanceof UserEvent.UserLoginSuccess){
            mLoginView.setVisibility(View.GONE);
        }else if (event instanceof UserEvent.UserLoginFail){
            mLoginView.setVisibility(View.VISIBLE);
        }
    }

    //// TODO: 17/1/17
    private void testBadge(){
        mBadgeView.setVisibility(View.VISIBLE);
        mBadgeView.setText("3");
    }
}
