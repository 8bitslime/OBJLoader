package net.slimeco.objloader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OBJModel {
	
	public static boolean OBJ_REVERSE_ORDER = false;
	
	private String filePath;
	private String fileContents = "";
	
	public List<OBJFace> faces = new ArrayList<OBJFace>();
	
	public OBJModel(String path) {
		String split[] = path.split("\\.");
		if (!split[split.length-1].equalsIgnoreCase("obj"))
			new Exception("Cannot load file type ." + split[split.length-1]).printStackTrace();
		else {
			filePath = path;
			try {
				for (String s : Files.readAllLines(Paths.get(filePath), Charset.defaultCharset()))
					fileContents += s + "\n";
				parseFileContents(fileContents);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void parseFileContents(String fileContents) {
		
		List<OBJVector3f> vertices      = new ArrayList<OBJVector3f>();
		List<OBJVector3f> textureCoords = new ArrayList<OBJVector3f>();
		List<OBJVector3f> normals       = new ArrayList<OBJVector3f>();
		List<Integer>     indices       = new ArrayList<Integer>();
		
		String[] lines = fileContents.split("\n");
		for (String line : lines) {
			line = line.trim();
			String[] tokens = line.split(" ");
			switch (tokens[0]) {
			case "v"  : //Vertex
				float x = Float.parseFloat(tokens[1]);
				float y = Float.parseFloat(tokens[2]);
				float z = Float.parseFloat(tokens[3]);
				vertices.add(new OBJVector3f(x, y, z));
				break;
			case "vt" : //Texture Coordinate
				float u = Float.parseFloat(tokens[1]);
				float v = Float.parseFloat(tokens[2]);
				textureCoords.add(new OBJVector3f(u, v));
				break;
			case "vn" : //Normal
				float xn = Float.parseFloat(tokens[1]);
				float yn = Float.parseFloat(tokens[2]);
				float zn = Float.parseFloat(tokens[3]);
				normals.add(new OBJVector3f(xn, yn, zn));
				break;
			case "f"  : //Face
				List<String[]> segments = new ArrayList<String[]>(); //Indexing for face building
				segments.add(tokens[1].split("/"));
				segments.add(tokens[2].split("/"));
				segments.add(tokens[3].split("/"));
				if (tokens.length > 4) {
					segments.add(tokens[3].split("/"));
					for (int i = 0; i < segments.size(); i++) { //Quadrilateral
						indices.add(Integer.parseInt(segments.get(i)[0])-1);
						indices.add(Integer.parseInt(segments.get(i)[1])-1);
						indices.add(Integer.parseInt(segments.get(i)[2])-1);
						indices.add(Integer.parseInt(segments.get(i)[0])-1);
						indices.add(Integer.parseInt(segments.get(i)[2])-1);
						indices.add(Integer.parseInt(segments.get(i)[3])-1);
					}
				} else {
					for (int i = 0; i < segments.size(); i++) { //Triangle
						indices.add(Integer.parseInt(segments.get(i)[0])-1);
						indices.add(Integer.parseInt(segments.get(i)[1])-1);
						indices.add(Integer.parseInt(segments.get(i)[2])-1);
					}
				}
				break;
			}
		}
		for (int i = 0; i < indices.size(); i+=9) { //Face Building
			OBJVector3f[] verts = new OBJVector3f[3];
			OBJVector3f[] texts = new OBJVector3f[3];
			OBJVector3f[] norms = new OBJVector3f[3];
			
			verts[0] = vertices.get(indices.get(i + 0)); //Vertices
			verts[1] = vertices.get(indices.get(i + 3));
			verts[2] = vertices.get(indices.get(i + 6));
			
			texts[0] = textureCoords.get(indices.get(i + 1)); //Texture Coordinates
			texts[1] = textureCoords.get(indices.get(i + 4));
			texts[2] = textureCoords.get(indices.get(i + 7));
			
			norms[0] = normals.get(indices.get(i + 2)); //Normals
			norms[1] = normals.get(indices.get(i + 5));
			norms[2] = normals.get(indices.get(i + 8));
			
			faces.add(new OBJFace(verts, texts, norms));
		}
	}
	
	public String toString() {
		String r = "";
		for (OBJFace f : faces)
			r += f.toString() + "\n";
		return r;
	}
}
