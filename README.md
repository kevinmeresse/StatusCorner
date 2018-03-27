# StatusCorner
Custom view displaying a corner overlay (default is top left) with an optional rotated text

<img src="/extra/screenshot.png" width="256">

### Usage

Add custom attributes for the StatusCorner view, in res/values/attrs.xml (create the file if it does not exist already):

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- ... -->

    <declare-styleable name="StatusCorner">
        <attr name="statusText" format="string" />
        <attr name="statusTextColor" format="color" />
        <attr name="statusTextSize" format="dimension" />
        <attr name="statusTextAllCaps" format="boolean" />
        <attr name="bgColor" format="color" />
        <attr name="cornerGravity">
            <flag name="top" value="48" />
            <flag name="start" value="8388611" />
            <flag name="bottom" value="80" />
            <flag name="end" value="8388613" />
        </attr>
    </declare-styleable>
</resources>
~~~

In your XML layout file (replace `com.km.myproject.customview` with your own package name):

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:background="#F9F9F9">

    <com.km.myproject.customview.StatusCorner
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="top|start"
        app:bgColor="#222222"
        app:cornerGravity="top|start"
        app:statusText="TEST"
        app:statusTextColor="#03A9F4"
        app:statusTextSize="20sp"/>

    <com.km.myproject.customview.StatusCorner
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="bottom|start"
        app:bgColor="#222222"
        app:cornerGravity="bottom|start"
        app:statusText="TEST"
        app:statusTextColor="#CDDC39"
        app:statusTextSize="20sp"/>

    <com.km.myproject.customview.StatusCorner
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="top|end"
        app:bgColor="#222222"
        app:cornerGravity="top|end"
        app:statusText="TEST"
        app:statusTextColor="#E91E63"
        app:statusTextSize="20sp"/>

    <com.km.myproject.customview.StatusCorner
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="bottom|end"
        app:bgColor="#222222"
        app:cornerGravity="bottom|end"
        app:statusText="TEST"
        app:statusTextColor="#FF5722"
        app:statusTextSize="20sp"/>

</FrameLayout>
~~~
