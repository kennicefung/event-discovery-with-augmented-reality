package com.goer.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.goer.controller.EventController;
import com.goer.helper.GlobalHelper;
import com.goer.view.CamActivity;
import com.goer.view.EventDetailActivity;
import com.wikitude.architect.ArchitectJavaScriptInterfaceListener;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;
import com.goer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import com.goer.helper.LocationProvider;


public class CamFragment extends AbstractArchitectCamFragment {

	/**
	 * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
	 */
	private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
	
	@Override
	public String getARchitectWorldPath() {
		try {
			final String decodedUrl = URLDecoder.decode(GlobalHelper.ArchitectWorldPath(), "UTF-8");
			return decodedUrl;
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(this.getActivity(), "Unexpected Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.fragment_cam;
	}

	@Override
	public int getArchitectViewId() {
		return R.id.architectView;
	}
	
	@Override
	public String getWikitudeSDKLicenseKey() {
		return GlobalHelper.ARKey();
	}
	

	@Override
	public SensorAccuracyChangeListener getSensorAccuracyListener() {
		return new SensorAccuracyChangeListener() {
			@Override
			public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
				if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && getActivity() != null && !getActivity().isFinishing()  && System.currentTimeMillis() - CamFragment.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
					Toast.makeText( getActivity(), R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
				}
			}
		};
	}

	@Override
	public ArchitectView.ArchitectWorldLoadedListener getWorldLoadedListener() {
		return new ArchitectView.ArchitectWorldLoadedListener() {
			@Override
			public void worldWasLoaded(String url) {
                Log.i("CamFragment", "worldWasLoaded: url: " + url);
			}

			@Override
			public void worldLoadFailed(int errorCode, String description, String failingUrl) {
				Log.e("CamFragment", "worldLoadFailed: url: " + failingUrl + " " + description);
			}
		};
	}

	@Override
	public ILocationProvider getLocationProvider(final LocationListener locationListener) {
		return new LocationProvider(this.getActivity(), locationListener);
	}

	@Override
	public ArchitectJavaScriptInterfaceListener getArchitectJavaScriptInterfaceListener() {
		return new ArchitectJavaScriptInterfaceListener() {
			@Override
			public void onJSONObjectReceived(JSONObject jsonObject) {
				try {
					switch (jsonObject.getString("action")) {
						case "present_poi_details":
							final Intent poiDetailIntent = new Intent(getActivity(), EventDetailActivity.class);
							poiDetailIntent.putExtra("EventModel", EventController.getEventByEventID(jsonObject.getString("id")));
							getActivity().startActivity(poiDetailIntent);
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

}
