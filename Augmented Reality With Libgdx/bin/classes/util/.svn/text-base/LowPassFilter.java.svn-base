package util;

public class LowPassFilter {

	float[] output;

	public LowPassFilter() {

	}

	// LowPass Filter
	public  float[] lowPass(float[] input, float alpha) {
		if (output == null){
			output= input;
			return output;
		}

		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + alpha * (input[i] - output[i]);
		}
		return output;
	}

}
