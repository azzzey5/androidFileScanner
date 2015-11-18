package com.asb.antivirus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends Activity {

	public static final String VIRUS_IDENTTITY = ".prop";

	private Button bQuick, bFull;

	private static final int quickScan = 0, fullScan = 1;

	private int fileScaned = 0;

	private int threatCount = 0;

	public static ArrayList<String> infectedFiles = new ArrayList<String>();

	public static ArrayList<String> scanedFiles = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		final Button bAboutUs = (Button) findViewById(R.id.btn_about_us);
		Button bSetting = (Button) findViewById(R.id.btn_default_setting);

		bQuick = (Button) findViewById(R.id.btn_quick_scan);
		bFull = (Button) findViewById(R.id.btn_full_scan);

		bAboutUs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(HomeActivity.this, AboutUs.class);
				startActivity(i);
			}
		});

		bFull.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
				pd.setTitle("Please wait !");
				pd.setMessage("Scanning...");
				pd.show();

				bAboutUs.postDelayed(new Runnable() {

					@Override
					public void run() {

						getScannedData(fullScan);

						pd.dismiss();
					}
				}, 5000);
			}
		});

		bQuick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
				pd.setTitle("Please wait !");
				pd.setMessage("Scanning...");
				pd.show();

				bAboutUs.postDelayed(new Runnable() {

					@Override
					public void run() {

						getScannedData(quickScan);

						pd.dismiss();
					}
				}, 5000);
			}
		});

		bSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder dialog = new AlertDialog.Builder(
						HomeActivity.this);
				dialog.setTitle("Alert !");
				dialog.setMessage("Do you want to set deault setting, all data will be earased, to continue click YES or to cancel click NO.");

				dialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								setDefaultSetting();
							}
						});

				dialog.setNegativeButton("NO", null);

				dialog.show();
			}
		});
	}

	private void getScannedData(int scanType) {

		try {

			File fileList;

			if (scanType == quickScan) {

				fileList = new File("/sdcard");
			} else {

				fileList = new File("/");
			}

			if (fileList != null) {

				File[] filenames = fileList.listFiles();

				for (int i = 0; i < filenames.length; i++) {

					fileScaned++;
					scanedFiles.add(String.valueOf(filenames[i]));

					Log.v("Main Directory", ": " + filenames.length);
					Log.d("Main Directory Name", ": " + filenames[i]);
					Log.d("************", "**************");

					if ((filenames[i].toString()).contains(VIRUS_IDENTTITY)) {

						infectedFiles.add(String.valueOf(filenames[i]));

						threatCount++;
						Log.e("Threat Found", ": " + filenames[i]);
					}

					if (filenames[i].isDirectory()) {

						File[] fl = filenames[i].listFiles();

						if (fl != null) {
							Log.v("Sub Directory", ": " + fl.length);

							for (int j = 0; j < fl.length; j++) {

								fileScaned++;
								scanedFiles.add(String.valueOf(filenames[i]));

								Log.e("Sub Directory Name", ": " + fl[j]);
								Log.e("************", "**************");

								try {
									File myFile = new File(
											String.valueOf(fl[j]));
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}

									Log.d("Buffer: ", ": " + aBuffer);

									if (aBuffer.contains(VIRUS_IDENTTITY)) {

										infectedFiles
												.add(String.valueOf(fl[j]));

										threatCount++;
										Log.e("Threat Found", ": " + fl[j]);
									}

									myReader.close();
								} catch (Exception e) {
									Toast.makeText(getBaseContext(),
											e.getMessage(), Toast.LENGTH_SHORT)
											.show();
								}
							}
						}
					}

					if (i == (filenames.length - 1)) {
						startActivity(new Intent(HomeActivity.this,
								ResultActivity.class).putExtra("fileScaned",
								fileScaned)
								.putExtra("threatCount", threatCount));
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void setDefaultSetting() {
		File cache = getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					Log.i("TAG",
							"**************** File /data/data/APP_PACKAGE/" + s
									+ " DELETED *******************");

					Toast.makeText(getApplicationContext(),
							"Default Setting has been done.", Toast.LENGTH_LONG)
							.show();

					Intent i = new Intent(HomeActivity.this, HomeActivity.class);
					startActivity(i);
					finish();
				}
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		fileScaned = 0;
		threatCount = 0;

		infectedFiles = new ArrayList<String>();
		scanedFiles = new ArrayList<String>();
	}
}
