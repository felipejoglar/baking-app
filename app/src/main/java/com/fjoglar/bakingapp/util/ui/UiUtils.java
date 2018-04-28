/*
 * Copyright 2018 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fjoglar.bakingapp.util.ui;

import com.fjoglar.bakingapp.R;

/**
 * Provides different types of imagery and text formatting for UI presentation.
 */
public class UiUtils {

    /**
     * Gets the specified image in function of the recipe
     *
     * @param recipeId recipe to look for the image
     * @return the image resource id for the requested recipe
     */
    public static int getImageResource(int recipeId) {
        switch (recipeId) {
            case 1:
                return R.drawable.nutella_pie;
            case 2:
                return R.drawable.brownies;
            case 3:
                return R.drawable.yellow_cake;
            case 4:
                return R.drawable.cheesecake;
        }
        return 0;
    }
}
