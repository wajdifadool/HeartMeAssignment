package com.tests.wajde.heartmeassignment.main_page;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tests.wajde.heartmeassignment.R;
import com.tests.wajde.heartmeassignment.data_manager.DownloadDataService;

import static com.tests.wajde.heartmeassignment.data_manager.Model.Attribuites.ACTION_RESP;
import static com.tests.wajde.heartmeassignment.data_manager.Model.Attribuites.KEY_RESULT;

/**
 * This is the main class and its responsible about submitting user data which
 * simulates blood test result
 */
public class ActivityMainBloodTest extends AppCompatActivity {

    private Intent mDownloadIntent;
    private ResponseReceiver mReceiver;

    FragmentBloodTestInput fragmentBloodTestInput;

    @Override
    protected void onStart() {

        //Broadcast Receiver to inform this activity when form the services
        IntentFilter filter = new IntentFilter(ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        registerReceiver(mReceiver, filter);
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_blood_test_result);

        //Fetch Data set with separate background thread
        mDownloadIntent = new Intent(this, DownloadDataService.class);
        startService(mDownloadIntent);
        fragmentBloodTestInput = FragmentBloodTestInput.newInstance();
        initFragment(fragmentBloodTestInput);
    }

    /**
     * @param fragment the fragment instance which will be added to the main frame layout
     *                 + backStack
     */
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
        ft.add(R.id.frame_layout, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    /**
     * @param fragment the fragment instance which will be replaced in the main frame layout
     */
    public void initFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .add(R.id.frame_layout,fragment ,  fragment.getClass().getSimpleName())
                .commit();
    }


    @Override
    protected void onStop() {

        unregisterReceiver(mReceiver);
        super.onStop();
    }

    /*Inner class*/
    public class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Ok, updates the activity’s upon the intent extra data packaged in the incoming result.
            boolean status = intent.getBooleanExtra(KEY_RESULT, false);
            //mm? Some stuff
            fragmentBloodTestInput.notifyDataDownloaded(status);
        }//[ End of onReceive() ]
    }//[ End of ResponseReceiver.java ]


}
