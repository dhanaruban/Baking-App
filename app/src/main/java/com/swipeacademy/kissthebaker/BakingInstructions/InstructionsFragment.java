package com.swipeacademy.kissthebaker.BakingInstructions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.swipeacademy.kissthebaker.Main.RecipeResponse;
import com.swipeacademy.kissthebaker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionsFragment extends Fragment {

    @BindView(R.id.ingredients_recyclerView)
    RecyclerView mIRecyclerView;
    @BindView(R.id.directions_recyclerView)
    RecyclerView mSRecyclerView;
    @BindView(R.id.instructions_fragment_layout)
    LinearLayout mLinearLayout;

    private static final String INGREDIENTS_LIST = "iList";
    private static final String STEPS_LIST = "sList";

    private ArrayList<RecipeResponse.IngredientsBean> iList;
    private ArrayList<RecipeResponse.StepsBean> sList;

    private IngredientsRecyclerAdapter ingredientsRecyclerAdapter;
    private DirectionsRecyclerAdapter directionsRecyclerAdapter;
    private Unbinder unbinder;

    public static InstructionsFragment newInstance(
            ArrayList<RecipeResponse.IngredientsBean> iList, ArrayList<RecipeResponse.StepsBean> sList) {
        InstructionsFragment fragment = new InstructionsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS_LIST, iList);
        args.putParcelableArrayList(STEPS_LIST, sList);
        fragment.setArguments(args);
        return fragment;
    }

    public interface InstructionCallBack{
        void onDirectionSelected(ArrayList<RecipeResponse.StepsBean> sList,
                                 int position);
    }

    public InstructionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iList = getArguments().getParcelableArrayList(INGREDIENTS_LIST);
        sList = getArguments().getParcelableArrayList(STEPS_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instructions, container, false);
        unbinder = ButterKnife.bind(this, view);

        ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getContext(), iList);
        mIRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mIRecyclerView.setAdapter(ingredientsRecyclerAdapter);
        mIRecyclerView.setNestedScrollingEnabled(false);

        directionsRecyclerAdapter = new DirectionsRecyclerAdapter(getContext(), sList);
        mSRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSRecyclerView.setAdapter(directionsRecyclerAdapter);
        mSRecyclerView.setNestedScrollingEnabled(false);

        directionsRecyclerAdapter.setOnInstructionClickListener(new DirectionsRecyclerAdapter.InstructionClickListener() {
            @Override
            public void onInstructionClicked(View view, int position) {

                ((InstructionCallBack)getActivity()).onDirectionSelected(sList,position);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
