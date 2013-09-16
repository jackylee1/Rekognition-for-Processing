package rekognition.faces;

import httprocessing.PostRequest;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;
//import processing.core.PImage;

public class RekognitionFace {

	String rekog_key = "";
	String rekog_secret = "";

	PApplet p5;

	public static final String api = "http://rekognition.com/func/api/";

	public RekognitionFace(PApplet p5_, String key, String secret) {
		p5 = p5_;
		rekog_key = key;
		rekog_secret = secret;
	}

	public Face[] detectFacesURL(String url) {
		PostRequest post = new PostRequest(api);
		post.addData("api_key", rekog_key);
		post.addData("api_secret", rekog_secret);
		post.addData("jobs","face_part_gender_emotion_age_glass");
		post.addFile("urls", url);
		post.send();
		String content = post.getContent();
		return facesFromJSON(content);
	}

	public Face[] detectFaces(File f) {
		PostRequest post = new PostRequest(api);
		post.addData("api_key", rekog_key);
		post.addData("api_secret", rekog_secret);
		post.addData("jobs","face_part_gender_emotion_age_glass");
		post.addFile("uploaded_file", f);
		post.send();
		String content = post.getContent();
		return facesFromJSON(content);
	}
	
	public Face[]  recognizeFace(String path) {
		PostRequest post = new PostRequest(api);
		post.addData("api_key", rekog_key);
		post.addData("api_secret", rekog_secret);
		// Should add these eventually, now just using default
		//post.addData("name_space",namespace);
		//post.addData("user_id",user_id);
		post.addData("job_list", "face_recognize_part_gender_emotion_age_glass");
		File f = new File(path);
		post.addFile("uploaded_file", f);
		post.send();
		String content = post.getContent();
		//System.out.println(content);
		return facesFromJSON(content);
	}

	public void addFace(String path, String name) {
		PostRequest post = new PostRequest(api);
		post.addData("api_key", rekog_key);
		post.addData("api_secret", rekog_secret);
		// Should add these eventually, now just using default
		//post.addData("name_space",namespace);
		//post.addData("user_id",user_id);
		post.addData("job_list", "face_add_[" + name + "]");
		File f = new File(path);
		post.addFile("uploaded_file", f);
		post.send();
		//String content = post.getContent();
		//System.out.println(content);	
	}
	

	
	public void train() {
		PostRequest post = new PostRequest(api);
		post.addData("api_key", rekog_key);
		post.addData("api_secret", rekog_secret);
		// Should add these eventually, now just using default
		//post.addData("name_space",namespace);
		//post.addData("user_id",user_id);
		post.addData("job_list", "face_train");
		post.send();
		//String content = post.getContent();
		//System.out.println(content);	
	}


	public Face[] detectFacesPath(String path) {
		File f = new File(path);
		// Not worrying about size for now
		/*long size = f.length();
		int maxsize = 1000000;
		if (size > maxsize) {
			String tempPath = "temp/temp.jpg";
			PImage img = p5.loadImage(path);
			float ratio = size/(float)maxsize;
			int w = (int) (img.width/ratio);
			int h = (int) (img.height/ratio);
			img.resize(w,h);
			System.out.println("Too big, resizing to: " + w + "," + h);
			img.save(tempPath);
			return detectFacesPath(tempPath);
		} else {
			return detectFaces(f);
		}*/
		return detectFaces(f);
	}



	public Face[] facesFromJSON(String content) {
		try {
			JSONObject data = new JSONObject(content);
			JSONArray facearray = data.getJSONArray("face_detection");

			Face[] faces = new Face[facearray.length()];
			for (int i = 0; i < faces.length; i++) {
				faces[i] = new Face();  // Fix to include width and height!
				faces[i].fromJSON(facearray.getJSONObject(i));
			}
			return faces;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
}
