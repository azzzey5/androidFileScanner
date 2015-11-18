package com.asb.antivirus;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		TextView tScan = (TextView) findViewById(R.id.tv_scaned);
		TextView tThread = (TextView) findViewById(R.id.tv_thread);

		if (getIntent().getExtras() != null) {

			int fileScaned = getIntent().getExtras().getInt("fileScaned");
			int threatCount = getIntent().getExtras().getInt("threatCount");

			tScan.setText("" + fileScaned);
			tThread.setText("" + threatCount);
		}

		findViewById(R.id.btnDelete).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (!HomeActivity.infectedFiles.isEmpty()) {

							for (int i = 0; i < HomeActivity.infectedFiles
									.size(); i++) {

								File file = new File(HomeActivity.infectedFiles
										.get(i));

								boolean b = deleteDir(file);
								if (b) {
									Toast.makeText(
											getApplicationContext(),
											HomeActivity.infectedFiles.get(i)
													+ " delete successfully.",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											HomeActivity.infectedFiles.get(i)
													+ " can not delete.",
											Toast.LENGTH_LONG).show();
								}
							}
						} else {

							Toast.makeText(getApplicationContext(),
									"Virus not found.", Toast.LENGTH_LONG)
									.show();
						}

					}
				});

		findViewById(R.id.btnLogs).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						ListView lv = (ListView) findViewById(R.id.lv_logs);

						if (!HomeActivity.scanedFiles.isEmpty()) {

							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									ResultActivity.this,
									android.R.layout.simple_list_item_1,
									HomeActivity.infectedFiles);

							lv.setAdapter(adapter);
						} else {

							Toast.makeText(getApplicationContext(),
									"Scan result not found.", Toast.LENGTH_LONG)
									.show();
						}

					}
				});
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

}
