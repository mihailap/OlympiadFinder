package com.mihailap.olympiadfinder.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.databinding.ActivityOlympiadInfoBinding;

public class OlympiadInfoActivity extends AppCompatActivity {
    private ActivityOlympiadInfoBinding binding;
    private static final String EXTRA_OLYMPIAD = "olympiad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOlympiadInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Olympiad olympiad = (Olympiad) getIntent().getSerializableExtra(EXTRA_OLYMPIAD);

        try {
            binding.tvName.setText(olympiad.getName());
            binding.tvSubject.setText(olympiad.getSubject());
            binding.tvGrade.setText(olympiad.getGradeRange());
            if (olympiad.getBvi()) {
                binding.tvBvi.setText("Yes");
            } else {
                binding.tvBvi.setText("No");
            }
            binding.tvRsosh.setText(String.valueOf(olympiad.getRsoshLevel()));
        } catch (Exception e) {
            Log.d("OLYMPIAD_INF", "Something went wrong!");
        }

        binding.btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://" + olympiad.getUrl()));
                startActivity(intent);
            }
        });

    }

    public static Intent newIntent(Context context, Olympiad olympiad) {
        Intent intent = new Intent(context, OlympiadInfoActivity.class);
        intent.putExtra(EXTRA_OLYMPIAD, olympiad);
        return intent;
    }


}