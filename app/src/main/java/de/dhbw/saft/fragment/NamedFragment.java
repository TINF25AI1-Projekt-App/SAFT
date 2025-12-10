/*
* Copyright 2025 SAFT Authors and Contributors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package de.dhbw.saft.fragment;

import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.dhbw.saft.HomeActivity;

/**
 * Abstract class, representing a {@link Fragment} with a
 * specific name. The name is displayed as a title in the
 * {@link Toolbar} at {@link HomeActivity}
 */
public abstract class NamedFragment extends Fragment {

	/**
	 * Gets the name of the current Fragment.
	 *
	 * @return	The name
	 */
	public abstract @NonNull String getName();
}
