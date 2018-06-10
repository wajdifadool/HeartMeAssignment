package com.tests.wajde.heartmeassignment.main_page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tests.wajde.heartmeassignment.R;
import com.tests.wajde.heartmeassignment.data_manager.Model;

import static com.tests.wajde.heartmeassignment.data_manager.Model.Arguments.TEST_CATEGORY;
import static com.tests.wajde.heartmeassignment.data_manager.Model.Arguments.TEST_NAME;
import static com.tests.wajde.heartmeassignment.data_manager.Model.Arguments.TEST_RESULT;
import static com.tests.wajde.heartmeassignment.data_manager.Model.UNKNOWN;


/**
 * Created by wajdi on 09/06/2018.
 */

public class FragmentBloodTestResult extends Fragment {


    /*member class references*/
    private View mView;
    private EditText editTextTestName;
    private EditText editTextTestValue;
    private TextView editTextEvaluated;
    private ImageView mImageReaction;

    public static FragmentBloodTestResult newInstance(Bundle args) {
        FragmentBloodTestResult fragment = new FragmentBloodTestResult();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_blood_test_result, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextTestName = mView.findViewById(R.id.edit_text_test_name);
        editTextTestValue = mView.findViewById(R.id.edit_text_test_result);
        editTextEvaluated = mView.findViewById(R.id.edit_text_test_analyzed);
        mImageReaction = mView.findViewById(R.id.image_view_reaction);
        model = Model.getINSTANCE();
        showResult();
    }


    /*----------------------- Business Logic Layer -----------------------*/
    /*-------------- Inner Methods --------------*/
    private void showResult() {
        //Data Manager.java class Instance which holds the DataSet
        Model model = Model.getINSTANCE();

        //Arguments which passed from the previous fragment
        String argName = getArguments().getString(TEST_NAME);
        String argCategory = getArguments().getString(TEST_CATEGORY);
        double argResult = getArguments().getDouble(TEST_RESULT);


        //qa
        Log.d("Finals", "category = " + argCategory);
        Log.d("Finals", "name= " + argName);
        Log.d("Finals", "result = " + argResult);


        int imgRes;

        if (argCategory == UNKNOWN) {
            editTextTestName.setText(argName);
            editTextTestValue.setText(String.valueOf(argResult));
            editTextEvaluated.setText(getString(R.string.unknown_test));
            //Unknown image
            imgRes = R.drawable.ic_face1;
        } else {
            //set texts
            editTextTestName.setText(argName);
            editTextTestValue.setText(String.valueOf(argResult));
            imgRes = model.evaluateResultstatus(argCategory, argResult)
                    ? R.drawable.sad_face : R.drawable.happy_face;
            analyzadats(argCategory, argResult);
        }

        //kept it simple, no use of 3rd party like Glide, Picasso etc
        mImageReaction.setImageResource(imgRes);
    }


    Model model;

    private void analyzadats(String category, double result) {
        int threshold = model.getThreshold(category);
        StringBuilder sb = new StringBuilder();
        sb.append("blood test category: ");
        sb.append(category);
        sb.append("\n");
        sb.append("threshold: ");
        sb.append(threshold);
        sb.append("\n");
        sb.append(thresholdAnalyzed(threshold, result));
        sb.append(", ");
        sb.append(model.evaluateResultString(category, result));
        sb.append("\n");
        editTextEvaluated.setText(sb.toString());

    }

    private String thresholdAnalyzed(int threshold, double result) {
        int percent = (int) ((result / threshold) * 100);

        if (result < threshold) {
            return "Result is " + percent + "% of the threshold";
        }
        if (result > threshold) {
            return "Result is " + percent + "% of the threshold";
        }
        if (result == threshold) {
            return "My test result is exactly as the threshold";
        }
        return "";
    }


}
