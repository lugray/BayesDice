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

import android.os.Bundle;
import android.preference.PreferenceFragment;
import ca.ugray.SeekBarDialogPreference;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		updateSummary(null);
	}
	
	public void updateSummary(String key){
		if(key==null || key.equals(SettingsActivity.PREF_DELAY)){
			SeekBarDialogPreference pref = (SeekBarDialogPreference) findPreference(SettingsActivity.PREF_DELAY);
			if (pref.getProgress()==0){
				pref.setSummary(getString(R.string.pref_delay_summary_special));
			} else if(pref.getProgress()==100){
				pref.setSummary(String.format(getString(R.string.pref_delay_summary), "1"));
			}else{
				pref.setSummary(String.format(getString(R.string.pref_delay_summary), "0."+String.format("%02d", pref.getProgress())));
			}
		}
		if(key==null || key.equals(SettingsActivity.PREF_PENALTY)){
			SeekBarDialogPreference pref = (SeekBarDialogPreference) findPreference(SettingsActivity.PREF_PENALTY);
			if (pref.getProgress()==0){
				pref.setSummary(getString(R.string.pref_penalty_summary_special));
			} else {
				pref.setSummary(Integer.toString(pref.getProgress()));
			}
		}

	}
}
