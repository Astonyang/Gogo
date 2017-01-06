package com.xxx.gogo.view.provider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxx.gogo.R;
import com.xxx.gogo.model.MainDatabaseHelper;
import com.xxx.gogo.model.provider.ProviderModel;
import com.xxx.gogo.utils.CommonUtils;

class ProviderAdapter extends BaseAdapter {
    private Context mContext;

    ProviderAdapter(Context context){
        mContext = context;
        ProviderModel.getInstance().setDbHelper(MainDatabaseHelper.getDataBaseHelper(mContext));
        ProviderModel.getInstance().checkIfNeedLoad();
    }

    @Override
    public int getCount() {
        return ProviderModel.getInstance().getCount();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.provider_item, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.imgView = (TextView) convertView.findViewById(R.id.contact_img);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.contact_name);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.textView.setText(ProviderModel.getInstance().getContactName(position));

        Drawable drawable = CommonUtils.getStateListDrawable(
                mContext.getResources().getColor(R.color.textColor),
                mContext.getResources().getColor(R.color.pressed_color));
        convertView.setBackgroundDrawable(drawable);
        convertView.setLongClickable(true);
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteProvider(position);
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProviderDetailActivity.class);
                intent.putExtra("id", ProviderModel.getInstance().getId(position));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private void deleteProvider(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.delete_provider);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ProviderModel.getInstance().deleteItem(pos);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class ViewHolder{
        TextView imgView;
        TextView textView;
    }
}
