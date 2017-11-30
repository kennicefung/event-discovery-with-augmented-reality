package com.goer.view;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.goer.R;
import com.goer.view.fragment.CamFragment;


/**
 * Demo Implementation of a fragment use of the Wikitude ARchitectView.
 *
 */
public class CamActivity extends FragmentActivity {

    public static final String ARCHITECT_ACTIVITY_EXTRA_KEY_URL = "url2load";

    @Override
    protected void onCreate( final Bundle icicle ) {
        super.onCreate( icicle );

        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        this.setVolumeControlStream( AudioManager.STREAM_MUSIC );

        this.setContentView( R.layout.activity_cam );

        this.findViewById( R.id.cam_content_parent ).setBackgroundColor( Color.BLACK );

        if ( icicle == null ) {
			/* start transaction to set required fragments */
            Intent i = getIntent();
            Bundle args = new Bundle();
            args.putSerializable("user_id", i.getStringExtra("user_id"));
            CamFragment c = new CamFragment();
            c.setArguments(args);
            final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.cam_content, c );
			/* commit transaction */
            fragmentTransaction.commit();
        }

    }


}