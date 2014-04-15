package libgdx;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;

public class Screen {
	
	protected HashMap<String,Shape> shapeMap = new HashMap<String,Shape>();
	protected ArrayList<Shape> shapes = new ArrayList<Shape>();
	protected ArrayList<Shape> toBeDrawed = new ArrayList<Shape>();
	protected ArrayList<Shape> drawn = new ArrayList<Shape>();

	protected Camera camera;
	protected GestureDetector gestureDetector;
	protected EventHandler eventHandler;

	protected Context context;
	
	Cube c;

	public Screen(Context context) {
		this.context = context;
		this.eventHandler = new EventHandler() {

			@Override
			public boolean tap(float x, float y, int count, int button) {
				return super.tap(x, y, count, button);

			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {

				return super.pan(x, y, deltaX, deltaY);
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {

				return super.zoom(initialDistance, distance);
			}

		};
		this.gestureDetector = new GestureDetector(this.eventHandler);
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void init(int width, int height) {
		camera = new LibGDXPerspectiveCamera(context, 30f, width, height);
		camera.far = 1000.0f;
		camera.near = 0.1f;
		camera.position.set(0.0f, 0.0f, 0.0f);
		c = new Cube(context);
		c.setTranslationMatrix(3, 0, 0);
		shapes.add(c);
		
		
	}

	public void onSurfaceChanged(int width, int height) {
		camera = new LibGDXPerspectiveCamera(context, 30f, width, height);
		camera.far = 1000.0f;
		camera.near = 0.1f;
		camera.position.set(0.0f, 0.0f, 0.0f);
	}

	public void dispose() {
		((LibGDXPerspectiveCamera) camera).dispose();
		for(Shape shape: shapes){
			shape.dispose();
		}
		shapes = null;
	}

	public void render() {
		for(Shape s :shapes)
			s.render();
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void addShape(Shape shape) {
		shapes.add(shape);
		shape.setParent(this);
	}

	public void clearShapes() {
		shapes.clear();

	}

	public void clearToBeDrawed() {
		toBeDrawed.clear();

	}

	public void removeShape(Shape shape) {
		shapes.remove(shape);

	}

	public Camera getCamera() {
		return camera;
	}

	public ArrayList<Shape> getShapes() {
		return shapes;
	}

	public ArrayList<Shape> getToBeDrawed() {
		return toBeDrawed;
	}
	
	public HashMap<String,Shape> getShapeMap() {
		return shapeMap;
	}

	public Context getContext() {
		return context;
	}

	public boolean isDrawn(Shape onMarkerShape) {

		return drawn.contains(onMarkerShape);
	}

}
