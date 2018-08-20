# HandlerCounter
counter for android implement by handler

[![](https://jitpack.io/v/XingRay/HandlerCounter.svg)](https://jitpack.io/#XingRay/HandlerCounter)

## How to import
To get a Git project into your build:

### Step 1. 
Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### Step 2. 
Add the dependency

```groovy
dependencies {
    implementation 'com.github.XingRay:HandlerCounter:0.0.4'
}
```

## How to use

sample 

```java

HandlerCounter mCounter;

mCounter = new HandlerCounter()
                .startValue(1)
                .endValue(100)
                .stepSize(1)
                .countInterval(1000)
                .countListener(new CountListener() {
                    @Override
                    public void onCount(long count) {
                        String text = count + "";
                        tvText.setText(text);
                        Log.i(TAG, text);
                    }
                })
                .repeatMode(RepeatMode.RESTART)
                .repeatCount(3)
                .start();

findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) {
		mCounter.start();
	}
});

findViewById(R.id.bt_stop).setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) {
		mCounter.stop();
	}
});

findViewById(R.id.bt_pause).setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) {
		mCounter.pause();
	}
});

findViewById(R.id.bt_restart).setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) {
		mCounter.restart();
	}
});

```

save/restore state in Activity or Fragment
```java
@Override
protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	Log.i(TAG, "onSaveInstanceState: ");

	if (mCounter != null) {
		mCounter.onSaveInstanceState(outState);
	}
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onRestoreInstanceState(savedInstanceState);
	Log.i(TAG, "onRestoreInstanceState: ");

	if (mCounter != null) {
		mCounter.onRestoreInstanceState(savedInstanceState);
	}
}
```

more features see sample in MainActivity