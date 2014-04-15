package com.blogspot.mesai0;

import libgdx.Renderer;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import cameracontrol.AndroidDeviceCameraController;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import combinedview.CameraActivity;

public class MainActivity extends CameraActivity {

	private AndroidDeviceCameraController cameraControl;
	private Renderer renderer;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		cfg.r = 8;
		cfg.g = 8;
		cfg.b = 8;
		cfg.a = 8;

		// Camera Part

		cameraControl = new AndroidDeviceCameraController(this);

		view = initializeForView(renderer = new Renderer(this, cameraControl),
				cfg);
		// keep the original screen size
		origWidth = graphics.getWidth();
		origHeight = graphics.getHeight();

		if (graphics.getView() instanceof SurfaceView) {
			SurfaceView glView = (SurfaceView) graphics.getView();
			// force alpha channel - I'm not sure we need this as the GL surface
			// is already using alpha channel
			glView.getHolder().setFormat(PixelFormat.RGBA_8888);
		}
		// we don't want the screen to turn off during the long image saving
		// process

		graphics.getView().setKeepScreenOn(true);

		main = (FrameLayout) findViewById(R.id.main_layout);
		main.addView(view, 0);
	}

	

	@Override
	protected void onDestroy() {
		renderer.dispose();
		super.onDestroy();
	}

}
