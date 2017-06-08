package com.fyales.android.demo.rx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fyales.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RxMainActivity extends AppCompatActivity {

    @BindView(R.id.rx_main_lv)
    ListView rxMainLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_main);
        ButterKnife.bind(this);

        String[] functions = new String[]{
                "简单实例",
                "变换，线程切换实例"
        };

        rxMainLv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                functions));

        rxMainLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(RxMainActivity.this,SimpleRxActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(RxMainActivity.this,LiftActivity.class));
                        break;
                    case 2:
                        break;
                    default:
                }
            }
        });
    }
}
