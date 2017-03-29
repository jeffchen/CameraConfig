package com.evenwell.cameraconfig.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evenwell.cameraconfig.R;
import com.evenwell.cameraconfig.viewholder.ConfigViewHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffchen on 2017/3/28.
 */
public class ConfigAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mConfigs = new ArrayList<>();
    private Method mSetSystemProperties;
    private Method mGetSystemProperties;

    public ConfigAdapter(Context context) {
        mContext = context;

        Class<?> cls = null;
        try {
            cls = Class.forName("android.os.SystemProperties");
            mSetSystemProperties = cls.getDeclaredMethod("set", new Class<?>[]{String.class, String.class});
            mGetSystemProperties = cls.getDeclaredMethod("get", new Class<?>[]{String.class});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.config_item, parent, false);
        return new ConfigViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ConfigViewHolder) holder).mTitle.setText(mConfigs.get(position));
        try {
            String value = (String) mGetSystemProperties.invoke(null,
                    new Object[] { mConfigs.get(position) });
            ((ConfigViewHolder) holder).mEditText.setText(value, TextView.BufferType.EDITABLE);
            ((ConfigViewHolder) holder).mEditText.setSelection(((ConfigViewHolder) holder).mEditText.getText().length());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        ((ConfigViewHolder) holder).mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mSetSystemProperties.invoke(null,
                            new Object[]{
                                    mConfigs.get(position),
                                    ((ConfigViewHolder) holder).mEditText.getText().toString()});

                    Toast.makeText(mContext, R.string.set_property_done, Toast.LENGTH_SHORT).show();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConfigs.size();
    }

    public void setConfigs(List<String> configs) {
        mConfigs = configs;
        notifyDataSetChanged();
    }
}
