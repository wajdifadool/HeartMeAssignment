package com.tests.wajde.heartmeassignment.main_page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tests.wajde.heartmeassignment.R;
import com.tests.wajde.heartmeassignment.Utils;
import com.tests.wajde.heartmeassignment.data_manager.Model;
import com.tests.wajde.heartmeassignment.main_page.adapters.MyTestNameAutoCompleteAdapter;

import java.util.ArrayList;
import java.util.Map;

import static com.tests.wajde.heartmeassignment.data_manager.Model.Arguments.TEST_CATEGORY;
import static com.tests.wajde.heartmeassignment.data_manager.Model.Arguments.TEST_NAME;
import static com.tests.wajde.heartmeassignment.data_manager.Model.Arguments.TEST_RESULT;


/**
 * Created by wajdi on 09/06/2018.
 */

public class FragmentBloodTestInput extends Fragment
        implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {


    /*Member class references*/
    private View mView;
    private AutoCompleteTextView editTextTestName;
    private EditText editTextTestValue;
    private Button btnSubmitBloodTest;
    private Map<String, Integer> mData;


    /*Auto Complete filter Adapter*/
    private MyTestNameAutoCompleteAdapter mInputAdapter;


    public static FragmentBloodTestInput newInstance() {
        Bundle args = new Bundle();
        FragmentBloodTestInput fragment = new FragmentBloodTestInput();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_blood_test_input, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextTestName = mView.findViewById(R.id.edit_text_test_name);
        editTextTestValue = mView.findViewById(R.id.edit_text_test_result);
        btnSubmitBloodTest = mView.findViewById(R.id.button_submit_blood_test_result);
        btnSubmitBloodTest.setOnClickListener(this);
    }

    /*----------------------- Business Logic Layer -----------------------*/
    /*---------------Edit text interface Callbacks---------------*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString().trim();
        if (input.length() != 0) {
            mInputAdapter.getFilter().filter(input);
        }
    }

    private String mCategory = "UnKnown";


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //store the selected category as class member variable in order to
        // pass in the FragmentBloodTestResult.java arguments
        mCategory = (String) parent.getItemAtPosition(position);
    }

    /*-------------- Inner Methods --------------*/
    private void initAutocompleteAdapter() {
            /*instantiate AutoCompleteAdapter*/

        mInputAdapter = new MyTestNameAutoCompleteAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList(Model.getINSTANCE().getDataSet().keySet()));
        /*data binding*/
        editTextTestName.setAdapter(mInputAdapter);
        /*callbacks*/
        editTextTestName.addTextChangedListener(this);
        editTextTestName.setOnItemClickListener(this);

    }

    /**
     * notified by the activity from broadcast receiver
     *
     * @param status data download susses status
     */
    public void notifyDataDownloaded(boolean status) {

        if (status)
            //call fragment function
            initAutocompleteAdapter();
        else {
            //Mmm, some stuff here ?
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit_blood_test_result:
                prepareTestResult();
                break;
            default:
                break;

        }
    }

    private void prepareTestResult() {
        //first check the input
        String testName = editTextTestName.getText().toString().toLowerCase().trim().replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
        if (testName.length() == 0) {
            //show animation missing data
            String msg = String.format("%s %s", getString(R.string.test_name), getString(R.string.is_missing));
            showToast(msg);
            return;
        }

        if (editTextTestValue.getText().toString().trim().length() == 0) {
            //show animation missing data
            String msg = String.format("%s %s", getString(R.string.test_result), getString(R.string.is_missing));
            showToast(msg);
            return;
        }
        //SetUp the FragmentBloodTestResult
        //input values
        String stTestName = editTextTestName.getText().toString().trim(); // initialize with default value
        double dTestResult = Double.valueOf(editTextTestValue.getText().toString().trim());

        //Bundle which will hold the user input
        Bundle bundle = new Bundle();

        bundle.putString(TEST_NAME, stTestName);
        bundle.putString(TEST_CATEGORY, validatecategory(stTestName));
        bundle.putDouble(TEST_RESULT, dTestResult);

        //instantiate fragment instance
        FragmentBloodTestResult fragmnetResult = FragmentBloodTestResult.newInstance(bundle);


        //Hide KeyBoard
        Utils.hideKeyBoard(getActivity());
        //Show the fragment to the Main UI
        ((ActivityMainBloodTest) getActivity()).addFragment(fragmnetResult);

        editTextTestName.setText("");
        editTextTestValue.setText("");
    }

    private String validatecategory(String stTestName) {

        /*if no selection from the dropdown list*/
        if (mCategory == "UnKnown") {
            return Model.getINSTANCE().identifyCategory(stTestName.replaceAll(" ", ""));

        } else {
            //selection from the drop down list accrued but changed later on
            if (stTestName.replaceAll(" ", "").contains(mCategory.toLowerCase())) {
                //its the same category with some extraa input  pass the category!

            } else {
                //changed the the category
                return Model.getINSTANCE().identifyCategory(stTestName);
            }
        }

        return mCategory;

    }

    void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
