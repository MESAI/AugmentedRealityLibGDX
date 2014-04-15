package libgdx;

import java.util.concurrent.Semaphore;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Matrix4;

public class LibGDXCameraOrientation {
	protected static final float CHANGE_FACT = 3.5f;
	private SensorManager sensorMan;
	private Sensor sensorAcce;
	private Sensor sensorMagn;
	private SensorEventListener listener;
	private float matrix[] = new float[16];
	private BufferAlgo1 accelBufferAlgo;
	private BufferAlgo1 magnetBufferAlgo;
	private float[] lookAt = { 0, 0, -100, 1 };
	private float[] up = { 0, 1, 0, 1 };
	private float[] position = { 0, 0, 0 };
	private float far;
	private Sensor sensorOrien;
	private boolean stable;
	private Sensor sensorLinAcce;
	private float orientation[] = new float[3];
	private float acceleration[] = new float[3];
	private float oldOrientation[];
	private float oldAcceleration[];
	private float rotation[];
	private float[] newMat;
	private Matrix4 matT;
	private Semaphore clear = new Semaphore(1);
	private boolean quit;

	public LibGDXCameraOrientation(float far, boolean isMarker) {
		accelBufferAlgo = new BufferAlgo1(0.1f, 0.2f);
		magnetBufferAlgo = new BufferAlgo1(0.1f, 0.2f);
		this.far = far;
	}

	public float[] getOldOrientation() {
		return rotation;
	}

	public void start(Context context) {
		listener = new SensorEventListener() {

			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}

			public void onSensorChanged(SensorEvent evt) {

				if (clear.tryAcquire()) {
					if (!quit) {
						int type = evt.sensor.getType();
						// Smoothing the sensor data a bit seems like a good
						// idea.
						if (type == Sensor.TYPE_MAGNETIC_FIELD) {
							orientation = lowPass(evt.values, orientation, 0.1f);
						} else if (type == Sensor.TYPE_ACCELEROMETER) {

							acceleration = lowPass(evt.values, acceleration,
									0.05f);
						}
						if (oldAcceleration != null || oldOrientation != null) {
							accelBufferAlgo.execute(oldAcceleration,
									acceleration);
							magnetBufferAlgo.execute(oldOrientation,
									orientation);
						} else {
							oldAcceleration = acceleration;
							oldOrientation = orientation;
						}

						if ((type == Sensor.TYPE_MAGNETIC_FIELD)
								|| (type == Sensor.TYPE_ACCELEROMETER)) {
							newMat = new float[16];
							SensorManager.getRotationMatrix(newMat, null,
									oldAcceleration, oldOrientation);
							SensorManager.remapCoordinateSystem(newMat,
									SensorManager.AXIS_Y,
									SensorManager.AXIS_MINUS_X, newMat);
							matT = new Matrix4(newMat).tra();
							float[] newLookAt = { 0, 0, -far, 1 };
							float[] newUp = { 0, 1, 0, 1 };
							Matrix4.mulVec(matT.val, newLookAt);
							Matrix4.mulVec(matT.val, newUp);
							matrix = matT.val;
							// lookAt[0] = Math.abs(newLookAt[0] - lookAt[0]) >
							// 30 ? newLookAt[0]
							// + position[0]
							// : lookAt[0];
							// lookAt[1] = Math.abs(newLookAt[1] - lookAt[1]) >
							// 30 ? newLookAt[1]
							// + position[1]
							// : lookAt[1];
							// lookAt[2] = Math.abs(newLookAt[2] - lookAt[2]) >
							// 30 ? newLookAt[2]
							// + position[2]:lookAt[2];

							lookAt[0] = newLookAt[0] + position[0];
							lookAt[1] = newLookAt[1] + position[1];
							lookAt[2] = newLookAt[2] + position[2];

							up = newUp;
							newLookAt = null;
							newUp = null;
						}
						clear.release();
					}
				}
			}
		};
		sensorMan = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		sensorAcce = sensorMan.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		sensorMagn = sensorMan.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
		sensorMan.registerListener(listener, sensorAcce,
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorMan.registerListener(listener, sensorMagn,
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorMan.registerListener(listener, sensorOrien,
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorMan.registerListener(listener, sensorLinAcce,
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	public boolean getStable() {
		return stable;
	}

	public float[] getMatrix() {
		return matrix;
	}

	public float[] getLookAt() {
		float[] out = new float[3];
		out[0] = lookAt[0];
		out[1] = lookAt[1];
		out[2] = lookAt[2];
		return out;
	}

	public float[] getUp() {
		float[] out = new float[3];
		out[0] = up[0];
		out[1] = up[1];
		out[2] = up[2];
		return out;
	}

	public void setLookAtOffset(float x, float y, float z) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}

	public void setUp() {

	}

	public void finish() {
		sensorMan.unregisterListener(listener);
		sensorMan = null;

		try {
			clear.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sensorAcce = null;
		sensorMagn = null;
		listener = null;
		matrix = null;
		accelBufferAlgo = null;
		magnetBufferAlgo = null;
		lookAt = null;
		up = null;
		position = null;
		sensorOrien = null;
		sensorLinAcce = null;
		orientation = null;
		acceleration = null;
		oldOrientation = null;
		oldAcceleration = null;
		rotation = null;
		matT = null;
		newMat = null;
		quit = true;
		clear.release();

	}

	// Filter Class1
	public class BufferAlgo1 {

		private final float a;
		private final float b;
		private final float m;
		private final float n;

		public BufferAlgo1(float a, float b) {
			this.a = a;
			this.b = b;
			m = 1f / (b - a);
			n = a / (a - b);
		}

		public boolean execute(float[] target, float[] values) {
			target[0] = morph(target[0], values[0]);
			target[1] = morph(target[1], values[1]);
			target[2] = morph(target[2], values[2]);
			return true;
		}

		/**
		 * @param v
		 * @param newV
		 * @return newT=t+f(|v-t|)
		 */
		private float morph(float v, float newV) {
			float x = newV - v;
			if (x >= 0) {
				if (x < a)
					return v; // v+0*x
				if (b <= x)
					return newV; // v+1*x
				return v + x * m + n;
			} else {
				if (-x < a)
					return v;
				if (b <= -x)
					return newV;
				return v + x * m + n;
			}
		}

	}

	// LowPass Filter
	protected float[] lowPass(float[] input, float[] output, float alpha) {
		if (output == null)
			return input;

		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + alpha * (input[i] - output[i]);
		}
		return output;
	}

}