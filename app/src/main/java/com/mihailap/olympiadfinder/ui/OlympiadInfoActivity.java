package com.mihailap.olympiadfinder.ui;

import static com.mihailap.olympiadfinder.R.drawable.baseline_star_border_24;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mihailap.olympiadfinder.R;
import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.databinding.ActivityOlympiadInfoBinding;

public class OlympiadInfoActivity extends AppCompatActivity {
    private ActivityOlympiadInfoBinding binding;
    private static final String EXTRA_OLYMPIAD = "olympiad";
    private OlympiadInfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOlympiadInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OlympiadInfoViewModel.class);

        Olympiad olympiad = (Olympiad) getIntent().getSerializableExtra(EXTRA_OLYMPIAD);

        setData(olympiad);
        setListeners(olympiad);

        viewModel.getFavouriteStatus(olympiad.getId(), true);

        viewModel.getIsFav().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavourite) {
                if (isFavourite) {
                    binding.favourite.setImageResource(R.drawable.baseline_star_24);
                } else {
                    binding.favourite.setImageResource(baseline_star_border_24);
                }
            }
        });

    }

    public void setData(Olympiad olympiad) {
        try {
            binding.tvName.setText(olympiad.getName());

            if (!olympiad.getDescription().isEmpty()) {
                binding.tvDescription.setText(olympiad.getDescription());
            } else {
                binding.tvDescription.setVisibility(View.GONE);
            }

            if (!olympiad.getSubject().isEmpty()) {
                binding.tvSubjects.setText(olympiad.getSubject());
            } else {
                binding.tvSubjects.setVisibility(View.GONE);
            }

            if (!olympiad.getGradeRange().isEmpty()) {
                binding.tvGrade.setText(olympiad.getGradeRange());
            } else {
                binding.tvGrade.setVisibility(View.GONE);
            }

            if (!olympiad.getStages().isEmpty() && !olympiad.getDates().isEmpty()) {
                String[] arr1 = olympiad.getStages().split(",");
                String[] arr2 = olympiad.getDates().split(",");

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < arr1.length; i++) {
                    result.append(arr1[i]).append("\n").append(arr2[i]);
                    if (i < arr1.length - 1) {
                        result.append("\n\n");
                    }
                }
                binding.tvStagesDates.setText(result);
            } else {
                binding.tvSchedule.setVisibility(View.GONE);
            }

            if (olympiad.isFavourite()) {
                binding.favourite.setImageResource(R.drawable.baseline_star_24);
            } else {
                binding.favourite.setImageResource(baseline_star_border_24);
            }

//            if (!olympiad.getIconURL().isEmpty()) {
//                Picasso.get().load("https://olimpiada.ru/" + olympiad.getIconURL()).into(binding.icon);
//            }

        } catch (Exception e) {
            Log.d("OLYMPIAD_INF", "Something went wrong!");
        }

    }

    public void setListeners(Olympiad olympiad) {
        binding.tvSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent();
            }
        });

        if (!olympiad.getUrl().isEmpty()) {
            binding.btnWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(olympiad.getUrl()));
                    startActivity(intent);
                }
            });
        } else {
            binding.btnWeb.setVisibility(View.GONE);
        }

        binding.btnOlru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://olimpiada.ru/activity/" + olympiad.getId()));
                startActivity(intent);
            }
        });

        binding.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getFavouriteStatus(olympiad.getId(), false);


            }
        });
    }

    public void toggleContent() {
        if (binding.expandedLayout.getVisibility() == View.VISIBLE) {
            binding.expandedLayout.setVisibility(View.GONE);
        } else {
            binding.expandedLayout.setVisibility(View.VISIBLE);
        }
    }

    public static Intent newIntent(Context context, Olympiad olympiad) {
        Intent intent = new Intent(context, OlympiadInfoActivity.class);
        intent.putExtra(EXTRA_OLYMPIAD, olympiad);
        return intent;
    }
}
