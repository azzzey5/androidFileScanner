package com.asb.antivirus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		new MyCountDown(4000, 1000).start();
	}

	private class MyCountDown extends CountDownTimer {

		public MyCountDown(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

			Intent i = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(i);

			finish();
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}
	}

}
