package combinedview;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class CameraActivity extends AndroidApplication {
	protected int origWidth;
	protected int origHeight;
	protected FrameLayout main;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	public void addView(View view, int index) {
		main.addView(view, index);
	}

	public void addView(View view) {
		main.addView(view);
	}

	public Handler getMainHandler() {
		return this.handler;
	}

	public void post(Runnable r) {
		handler.post(r);
	}

	public void setFixedSize(int width, int height) {
		if (graphics.getView() instanceof SurfaceView) {
			SurfaceView glView = (SurfaceView) graphics.getView();
			glView.getHolder().setFormat(PixelFormat.RGBA_8888);
			glView.getHolder().setFixedSize(width, height);
		}
	}

	public void restoreFixedSize() {
		if (graphics.getView() instanceof SurfaceView) {
			SurfaceView glView = (SurfaceView) graphics.getView();
			glView.getHolder().setFormat(PixelFormat.RGBA_8888);
			glView.getHolder().setFixedSize(origWidth, origHeight);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean result;
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			result = true;
			break;

		default:
			result = super.dispatchKeyEvent(event);
			break;
		}

		return result;
	}

}