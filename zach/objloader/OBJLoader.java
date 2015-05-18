package zach.objloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJLoader {
	
	private OBJLoader() {}
	
	public static boolean REVERSE_FACE_ORDER = false;
	
	public static Map<String, OBJModel> load(String fileName) {
		try {
			Map<String, OBJModel> models = new HashMap<String, OBJModel>();
			
			List<Float>   positions   = new ArrayList<Float>();
			List<Float>   textCoords  = new ArrayList<Float>();
			List<Float>   normals     = new ArrayList<Float>();
			List<Integer> faceIndices   = new ArrayList<Integer>();
			
			String currentName = "";
			OBJModel currentModel = null;
			for (String s : Files.readAllLines(Paths.get(fileName))) {
				if (s.startsWith("o ")) {
					if (!currentName.equalsIgnoreCase("")) {
						//Face Building
						List<Float> finalVertices = faceBuilder(positions, textCoords, normals, faceIndices);
						Integer[] indices = new Integer[finalVertices.size() / 8];
						for(int i = 0; i < indices.length / 8; i++)
							indices[i] = i;

						currentModel = new OBJModel(finalVertices.toArray(new Float[0]), indices);

						//currentModel = new OBJModel(positions.toArray(new Float[0]), textCoords.toArray(new Float[0]),
						//normals.toArray(new Float[0]), indices.toArray(new Integer[0]));

						models.put(new String(currentName), (OBJModel)currentModel.clone());
						
						positions.clear();
						textCoords.clear();
						normals.clear();
						faceIndices.clear();
					}
					currentName = s.substring(2);
				}
				else if (s.startsWith("v ")) { //Vertex Position
					String[] strings = s.substring(2).split(" ");
					positions.add(Float.parseFloat(strings[0]));
					positions.add(Float.parseFloat(strings[1]));
					positions.add(Float.parseFloat(strings[2]));
				}
				else if (s.startsWith("vt ")) { // Texture Coords
					String[] strings = s.substring(3).split(" ");
					textCoords.add(Float.parseFloat(strings[0]));
					textCoords.add(Float.parseFloat(strings[1]));
				}
				else if (s.startsWith("vn ")) { //Normals
					String[] strings = s.substring(3).split(" ");
					normals.add(Float.parseFloat(strings[0]));
					normals.add(Float.parseFloat(strings[1]));
					normals.add(Float.parseFloat(strings[2]));
				}
				else if (s.startsWith("f ")) { //Faces
					String[] strings = s.substring(2).split(" ");
					String[] vert1 = strings[0].split("/");
					String[] vert2 = strings[1].split("/");
					String[] vert3 = strings[2].split("/");
					String[] vert4 = null;
					try {
						vert4 = strings[3].split("/");
					} catch (ArrayIndexOutOfBoundsException e) {}
					
					if (strings.length > 3) {
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[0] : vert3[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[1] : vert3[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[2] : vert3[2])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[0] : vert2[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[1] : vert2[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[2] : vert2[2])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[0] : vert1[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[1] : vert1[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[2] : vert1[2])-1);
						
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[0] : vert4[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[1] : vert4[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[2] : vert4[2])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[0] : vert3[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[1] : vert3[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[2] : vert3[2])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert4[0] : vert1[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert4[1] : vert1[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert4[2] : vert1[2])-1);
					} else {
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[0] : vert3[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[1] : vert3[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[2] : vert3[2])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[0] : vert2[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[1] : vert2[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[2] : vert2[2])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[0] : vert1[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[1] : vert1[1])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[2] : vert1[2])-1);
					}
				}
			}
			if (currentModel == null) {
				//Face Building
				List<Float> finalVertices = faceBuilder(positions, textCoords, normals, faceIndices);
				Integer[] indices = new Integer[finalVertices.size() / 8];
				for(int i = 0; i < indices.length / 8; i++)
					indices[i] = i;

				currentModel = new OBJModel(finalVertices.toArray(new Float[0]), indices);

				//currentModel = new OBJModel(positions.toArray(new Float[0]), textCoords.toArray(new Float[0]),
				//normals.toArray(new Float[0]), indices.toArray(new Integer[0]));

				models.put(new String(currentName), (OBJModel)currentModel.clone());
			}
			return models;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static final List<Float> faceBuilder(List<Float> verts, List<Float> texts, List<Float> norms, List<Integer> indices) {
		List<Float> output = new ArrayList<Float>();
		for (int i = 0; i < indices.size(); i += 3) {
			output.add(verts.get(((indices.get(i+0))*3)+0));
			output.add(verts.get(((indices.get(i+0))*3)+1));
			output.add(verts.get(((indices.get(i+0))*3)+2));
			
			output.add(texts.get(((indices.get(i+1))*2)+0));
			output.add(texts.get(((indices.get(i+1))*2)+1));
			
			output.add(norms.get(((indices.get(i+2))*3)+0));
			output.add(norms.get(((indices.get(i+2))*3)+1));
			output.add(norms.get(((indices.get(i+2))*3)+2));
		}
		return output;
	}
	
}
