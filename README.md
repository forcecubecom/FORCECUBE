FORCECUBE
=========

An Android library wich tracks iBeacons and sends data to server.


Documentation
=========

Open the [wiki](https://github.com/forcecubecom/FORCECUBE/wiki) for detailed documentation.

Basic Usage
=========

SDK provides "ForceCube" class, this is the only class which you should use.

Here are the 2 methods which provides "ForceCube" class

```java
ForceCube.getInstance().startService(this, "<your_key_here>", callback, Config.defaultConfiguration());
ForceCube.getInstance().addBeaconEventListener(new ForceCube.BeaconEventListener() {
	@Override
	public void onEnterRegionAction(Event event) {
		Log.d("sample", "onEnterRegionAction");
	}

	@Override
	public void onExitRegionAction(Event event) {
		Log.d("sample", "onExitRegionAction");
	}

	@Override
	public void onRangeAction(Event event) {
		Log.d("sample", "onRangeAction");
	}
});
```

Steps to setup library in Android Studio
=========

1. Download library archive

2. From Android Studio open **File** > **New Module** and select **Import .JAR or .AAR Package** then select downloaded file and hit finish

3. Now you must have new module named forcecube in your project

4. Open your project settings from **File** > **Project Settings** and under modules select your main module, then select **Dependencies** tab hit plus then **Module dependency** and select forcecube module from the list and hit ok

![Capture.PNG](https://raw.githubusercontent.com/forcecubecom/FORCECUBE/master/2888509329-Capture.png)
