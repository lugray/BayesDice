package ca.ugray.bayesdice;

/*
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static SharedPreferences prefs = null;
	private static Roller roller = new Roller();
	private static Roller pastRoller = null;
	public static Context context;

	Handler timerHandler = new Handler();
	Runnable revealRoll = new Runnable() {
		@Override
		public void run() {
			//findViewById(R.id.button_roll).setEnabled(true);
			findViewById(R.id.spinner).setVisibility(View.GONE);
			findViewById(R.id.textView_result).setVisibility(View.VISIBLE);
		}
	};

	public static void toast(String s){
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		updateResult(true);
		updatePlot();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_undo).setVisible(pastRoller!=null);
		return true;
	}

	public void openSettings(){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_undo:
			roller=pastRoller;
			pastRoller=null;
			updateResult(true);
			updatePlot();
			this.invalidateOptionsMenu();
			return true;
		case R.id.action_reset:
			pastRoller=roller;
			roller=new Roller();
			updateResult();
			updatePlot();
			this.invalidateOptionsMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void updatePlot(){
		int max = roller.max();
		for (int i=2;i<=12;i++){
			View top = findViewById(getResources().getIdentifier("plot_bar_"+Integer.toString(i)+"_top","id", getPackageName()));
			View bottom = findViewById(getResources().getIdentifier("plot_bar_"+Integer.toString(i)+"_bottom","id", getPackageName()));
			int c = roller.count(i);
			top.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,max-c));
			bottom.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,c));
		}
	}

	public void updateResult(){
		updateResult(false);
	}

	public void updateResult(boolean hideHint){
		TextView result = (TextView) findViewById(R.id.textView_result);
		if (roller.lastRoll()==1){
			result.setText(getString(R.string.result_startText));
		}else{
			if(!hideHint && pastRoller == null &&result.getText().toString().equals(getString(R.string.result_startText))){
				toast(getString(R.string.toast_instruction_roll));
			}
			result.setText(Integer.toString(roller.lastRoll()));
		}
	}

	/** Called when the user clicks the Roll button */
	public void doRoll(View view) {
		int delay = 10*prefs.getInt(SettingsActivity.PREF_DELAY,0);
		roller.setPenalty(prefs.getInt(SettingsActivity.PREF_PENALTY,10));
		roller.roll();
		updatePlot();
		if (delay==0){
			updateResult();
			if (pastRoller!=null){
				pastRoller=null;
				this.invalidateOptionsMenu();
			}
		}else{
			//findViewById(R.id.button_roll).setEnabled(false);
			findViewById(R.id.textView_result).setVisibility(View.GONE);
			findViewById(R.id.spinner).setVisibility(View.VISIBLE);
			updateResult();
			if (pastRoller!=null){
				pastRoller=null;
				this.invalidateOptionsMenu();
			}
			timerHandler.postDelayed(revealRoll, delay);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		timerHandler.removeCallbacks(revealRoll);
	}

}
