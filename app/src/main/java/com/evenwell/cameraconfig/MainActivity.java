package com.evenwell.cameraconfig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.evenwell.cameraconfig.adapter.ConfigAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DIRECTORY_PATH = "/sdcard/camera_config/";

    private Spinner mSpinner;
    private ArrayAdapter<String> mConfigAdapter;
    private String[] mConfigs;

    private RecyclerView mRecyclerView;
    private ConfigAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<File> files = getFileList();
        mConfigs = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            mConfigs[i] = files.get(i).getName();
        }

        mSpinner = (Spinner) findViewById(R.id.config_spinner);
        mConfigAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mConfigs);

        mRecyclerView = (RecyclerView) findViewById(R.id.config_content);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSpinner.setAdapter(mConfigAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateConfigFile(mConfigs[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdapter = new ConfigAdapter(getApplicationContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<File> getFileList() {
        File file = new File(DIRECTORY_PATH);
        return Arrays.asList(file.listFiles());
    }

    private void updateConfigFile(String fileName) {
        readConfig(fileName);
    }

    private void readConfig(String fileName) {
        List<String> configs = new ArrayList<>();
        BufferedReader reader = null;
        try {
            File file = new File(DIRECTORY_PATH + fileName);
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                configs.add(mLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mAdapter.setConfigs(configs);
    }
}
