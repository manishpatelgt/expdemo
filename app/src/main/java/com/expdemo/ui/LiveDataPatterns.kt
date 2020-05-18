/*
 * Copyright 2020 Manish Patel. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Manish Patel on 5/16/2020.
 */
//https://twitter.com/igorwojda/status/1261378680967688192/photo/1

/** different ways to expose LiveData from ViewModel to a View (Activity/Fragment) **/
enum class State {}

class LiveDataPatterns : ViewModel() {

    //Solution 1 - make MutableLiveData public
    //This approach works, but this is a bad idea because view can modify the LiveData Object
    val liveData1 = MutableLiveData<State>()

    //Solution 2 - let's make LiveData public (expose it instead of MutableLiveData)
    //Now, from view perspective this solution looks fine, but we have a problem, because we need MutableLiveData withing ViewModel
    // to put/post values to the stream (we can't post values to LiveData)
    val liveData2 = MutableLiveData<State>() as LiveData<State>

    //Let's capture our requirements:
    //1. We need to expose (immutable) LiveData to the View.
    //So it cannot edit the data itself.
    //2. We need to access MutableLiveData from ViewModel to put/post new values.
    //Now, let's consider few appropriate solutions

    //Solution 3
    //Let's name mutable live data using underscore prefix
    private val _liveData3 = MutableLiveData<State>()
    val liveData3 = _liveData3 as LiveData<State>

    //Solution 4
    //We can also perform casting by specifying type for a variable
    //(we can do it because MutableLiveData extend LiveData)
    private val _liveData4 = MutableLiveData<State>()
    val liveData4: LiveData<State> = _liveData4

    //Solution 5
    //Starting from Kotlin 1.4-M.2 we can delegate call to another property
    private val _liveData5 = MutableLiveData<State>()
    //val liveData5 by this::_liveData5

    //Solution 6
    //These above solutions  work quite well, but we could do even better by
    //defining custom asLiveData extension function.
    private val _liveData6 = MutableLiveData<State>()
    val liveData6 = _liveData6.asLiveData()

    fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

    //Amount of code is similar, but notice that this approach works much better with code completion

    //Solution 7 (IMO best)
    //We can also use alternative naming convention - use "mutableLiveData" as variable for mutable live data instead of using underscore prefix
    private val mutableLiveData7 = MutableLiveData<State>()
    val liveData7 = mutableLiveData7.asLiveData()

    //BTW
    //We could also expose getLiveData() method, but liveData is a state not an action.

    //Solution 9
    //This does not create backing field for the property
    //(more optimised bit still Solution 7 is easier to use)
    private val _liveData9 = MutableLiveData<State>()
    val liveData9 get() = _liveData9 as LiveData<State>

    //https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMS40LU0yIiwiY29kZSI6ImludGVyZmFjZSBMaXZlRGF0YVxuXG5jbGFzcyBNdXRhYmxlTGl2ZURhdGE6IExpdmVEYXRhIHtcbiAgIGZ1biBwb3N0VmFsdWUoKXt9XG59XG5cbmNsYXNzIFZpZXdNb2RlbCB7XG4gICAgcHJpdmF0ZSB2YWwgbXV0YWJsZUxpdmVEYXRhID0gTXV0YWJsZUxpdmVEYXRhKClcbiAgICBcbiAgICAvLyBPbmx5IGV4cG9zZSB0aGlzXG4gICAgdmFsIGxpdmVEYXRhOiBMaXZlRGF0YSBnZXQoKSA9IG11dGFibGVMaXZlRGF0YVxufVxuXG5mdW4gbWFpbigpIHtcbiAgICB2YWwgdmlld01vZGVsID0gVmlld01vZGVsKClcbiAgICB2aWV3TW9kZWwubGl2ZURhdGEucG9zdFZhbHVlKClcbn0iLCJwbGF0Zm9ybSI6ImphdmEiLCJhcmdzIjoiIn0=
}