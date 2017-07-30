package com.swipeacademy.kissthebaker.Main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swipeacademy.kissthebaker.BakingInstructions.InstructionsActivity;
import com.swipeacademy.kissthebaker.MySingleton;
import com.swipeacademy.kissthebaker.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    @BindView(R.id.recipe_recyclerView)RecyclerView mRecyclerView;
    @BindView(R.id.main_progress_bar)ProgressBar mProgressBar;

    private List<RecipeResponse> recipeResponseList;
    private RecipeRecyclerAdapter adapter;
    private Unbinder unbinder;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        unbinder = ButterKnife.bind(this,view);


        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        // Volley to retrieve date
        StringRequest recipeRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Set up Gson to parse Json
                Gson gson = new GsonBuilder().create();
                Type recipeListType = new TypeToken<ArrayList<RecipeResponse>>(){}.getType();
                // Update recipe list
                recipeResponseList = gson.fromJson(response,recipeListType);

                // Setup adapter and RecyclerView
                adapter = new RecipeRecyclerAdapter(getContext(),recipeResponseList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(adapter);

                // Set up recycler item click listener
                adapter.setOnItemClickListener(new RecipeRecyclerAdapter.RecipeCardClickListener() {
                    @Override
                    public void onRecipeCardClicked(View view, int position) {

                        Intent intent = new Intent(getActivity(), InstructionsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("recipeName",recipeResponseList.get(position).getName());
                        bundle.putParcelableArrayList("ingredientsList",recipeResponseList.get(position).getIngredients());
                        bundle.putParcelableArrayList("stepsList", recipeResponseList.get(position).getSteps());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                // Set progressBar GONE when data is retrieved and shown
                mProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add String request to queue
        MySingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(recipeRequest);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}