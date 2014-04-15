package cameracontrol;

import java.util.List;

import android.hardware.Camera;
import android.view.Display;
import android.view.ViewGroup;
import android.view.ViewParent;

import combinedview.CameraActivity;

public class AndroidDeviceCameraController implements DeviceCameraControl,
		Camera.PictureCallback, Camera.AutoFocusCallback {

//	private static final int ONE_SECOND_IN_MILI = 1000;
	private final CameraActivity activity;
	private CameraSurface cameraSurface;

	public AndroidDeviceCameraController(CameraActivity activity) {
		this.activity = activity;
	}

	@Override
	public synchronized void prepareCamera() {
		Display display = activity.getWindowManager().getDefaultDisplay();
		activity.setFixedSize(display.getWidth(), display.getHeight());

		if (cameraSurface == null) {
			cameraSurface = new CameraSurface(activity);
		}
		// activity.addContentView( cameraSurface, new LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
		activity.addView(cameraSurface, 0);
	}

	@Override
	public synchronized void startPreview() {
		// ...and start previewing. From now on, the camera keeps pushing
		// preview
		// images to the surface.
		if (cameraSurface != null && cameraSurface.getCamera() != null) {
			cameraSurface.getCamera().startPreview();
		}
	}

	@Override
	public synchronized void stopPreview() {
		// stop previewing.
		if (cameraSurface != null) {
			ViewParent parentView = cameraSurface.getParent();
			if (parentView instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) parentView;
				viewGroup.removeView(cameraSurface);
				
			}
			if (cameraSurface.getCamera() != null) {
				cameraSurface.getCamera().stopPreview();
			}
			cameraSurface = null;
		}
		activity.restoreFixedSize();
	}

	public void setCameraParametersForPicture(Camera camera) {
		// Before we take the picture - we make sure all camera parameters are
		// as we like them
		// Use max resolution and auto focus
		Camera.Parameters p = camera.getParameters();
		List<Camera.Size> supportedSizes = p.getSupportedPictureSizes();
		int maxSupportedWidth = -1;
		int maxSupportedHeight = -1;
		for (Camera.Size size : supportedSizes) {
			if (size.width > maxSupportedWidth) {
				maxSupportedWidth = size.width;
				maxSupportedHeight = size.height;
			}
		}
		p.setPictureSize(maxSupportedWidth, maxSupportedHeight);
		p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		camera.setParameters(p);
	}

	@Override
	public synchronized void onAutoFocus(boolean success, Camera camera) {
		// Focus process finished, we now have focus (or not)
		if (success) {
			if (camera != null) {
				camera.stopPreview();
				// We now have focus take the actual picture
				camera.takePicture(null, null, null, this);
			}
		}
	}

	@Override
	public void prepareCameraAsync() {
		Runnable r = new Runnable() {
			public void run() {
				prepareCamera();
			}
		};
		activity.post(r);
	}

	@Override
	public synchronized void startPreviewAsync() {
		Runnable r = new Runnable() {
			public void run() {
				startPreview();
			}
		};
		activity.post(r);
	}

	@Override
	public synchronized void stopPreviewAsync() {
		Runnable r = new Runnable() {
			public void run() {
				stopPreview();
			}
		};
		activity.post(r);
	}

	@Override
	public boolean isReady() {
		if (cameraSurface != null && cameraSurface.getCamera() != null) {
			return true;
		}
		return false;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		
		
	}
}