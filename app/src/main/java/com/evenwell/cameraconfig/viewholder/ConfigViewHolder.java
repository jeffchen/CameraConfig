package com.evenwell.cameraconfig.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evenwell.cameraconfig.R;

/**
 * Created by jeffchen on 2017/3/28.
 */
public class ConfigViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitle;
    public EditText mEditText;
    public Button mButton;

    public ConfigViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(R.id.config_title);
        mEditText = (EditText) itemView.findViewById(R.id.config_value);
        mButton = (Button) itemView.findViewById(R.id.config_submit);
    }
}
