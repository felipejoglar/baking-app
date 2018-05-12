# ![Baking App](https://raw.githubusercontent.com/fjoglar/baking-app/master/app/src/main/res/mipmap-mdpi/ic_launcher_round.png) Baking App

[![License Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://github.com/fjoglar/baking-app/blob/master/LICENSE.txt)
[![fjoglar twitter](https://img.shields.io/badge/twitter-@felipejoglar-blue.svg)](http://twitter.com/felipejoglar)
[![Platform Android](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)

This app has been made as part of the Android Developer Nanodegree from Udacity. It has been build using a Model-View-Presenter (MVP) approach with a domain layer.

The data layer is accessed via Repository pattern and make use of `Room` to abstract the local `SQLite` database access.

## Features

The app downloads a list of recipes from an Internet resource and stores it locally for offline usage.

It shows the ingredients, the steps to take to its completion in a beautiful and elegant way, also makes use of `ExoPlayer` to play the recipe's videos to have an easy recipe following by the user.

<p align="center">
<img src="https://github.com/fjoglar/baking-app/blob/master/assets/phone_mode.png" alt="Phone" style="width: 10px;"/>
</p>

<p align="center">
<img src="https://github.com/fjoglar/baking-app/blob/master/assets/tablet_mode.png" alt="Tablet" style="width: 10px;"/>
</p>


## How to build the app

1. Clone this repository in your local machine:

```
git clone https://github.com/fjoglar/baking-app.git
```

2. Open Android Studio and open the project from `File > Open...`

3. Run the project


## Languages, libraries and tools used

* [Java](https://docs.oracle.com/javase/8/)
* Android Support Libraries
* [RxJava2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
* [Retrofit](https://github.com/square/retrofit)
* [Gson](https://github.com/google/gson)
* [Picasso](https://github.com/square/picasso)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room)
* [ExoPlayer](https://developer.android.com/guide/topics/media/exoplayer)
* [Espresso](https://developer.android.com/training/testing/espresso/)


## Requirements

* JDK 1.8
* [Android SDK](https://developer.android.com/studio/index.html)
* Android O ([API 27](https://developer.android.com/preview/api-overview.html))
* Latest Android SDK Tools and build tools.


## License

```
Copyright 2018 Felipe Joglar Santos

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

>##### **NOTE:** This project was submitted by Felipe Joglar as part of the Android Developer Nanodegree At Udacity.
>##### As part of Udacity Honor code, your submissions must be your own work, hence submitting this project as yours will cause you to break the Udacity Honor Code and the suspension of your account.
>##### Me, the author of the project, allow you to check the code as a reference, but if you submit it, it's your own responsibility if you get expelled.
>##### Besides this notice, the above license applies and this license notice must be included in all works derived from this project.

>##### **NOTE 2:** The images from the logo and splash screen used within this app were [designed by Freepik](https://www.freepik.es/vector-gratis/coleccion-de-logos-de-panaderia-en-estilo-vintage_1893533.htm).