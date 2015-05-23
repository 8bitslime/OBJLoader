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
			
			boolean noTextureCoords = false;
			boolean noNormals = false;
			
			String currentName = "";
			OBJModel currentModel = null;
			for (String s : Files.readAllLines(Paths.get(fileName))) {
				if (s.startsWith("o ")) {
					if (!currentName.equalsIgnoreCase("")) {
						//Face Building
						List<Float> finalVertices = faceBuilder(positions, textCoords, normals, faceIndices,
								!noTextureCoords, !noNormals);
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
						
						noTextureCoords = false;
						noNormals = false;
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
					int pos = 3;
					while (strings[0].equalsIgnoreCase(""))
						strings = s.substring(pos++).split(" ");
					
					int textPos = 1;
					int normalPos = 2;
					
					String[] vert1 = null;
					String[] vert2 = null;
					String[] vert3 = null;
					String[] vert4 = null;
					
					if (!strings[0].contains("/")) {
						noTextureCoords = true;
						textPos = 0;
						noNormals = true;
						normalPos = 0;
						vert1 = new String[1]; vert1[0] = strings[0];
						vert2 = new String[1]; vert2[0] = strings[0];
						vert3 = new String[1]; vert3[0] = strings[0];
						try {
							vert4 = new String[1]; vert4[0] = strings[0];
						} catch (ArrayIndexOutOfBoundsException e) {}
					} else if (strings[0].contains("//")) {
						noTextureCoords = true;
						textPos = 0;
						normalPos = 1;
						vert1 = strings[0].split("//");
						vert2 = strings[1].split("//");
						vert3 = strings[2].split("//");
						try {
							vert4 = strings[3].split("//");
						} catch (ArrayIndexOutOfBoundsException e) {}
					} else {
						vert1 = strings[0].split("/");
						vert2 = strings[1].split("/");
						vert3 = strings[2].split("/");
						try {
							vert4 = strings[3].split("/");
						} catch (ArrayIndexOutOfBoundsException e) {}
					}
					
					if (strings.length > 3) {
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[0] 			: vert3[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[textPos] 	: vert3[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[normalPos]  : vert3[normalPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[0] 			: vert2[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[textPos] 	: vert2[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[normalPos]  : vert2[normalPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[0] 			: vert1[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[textPos] 	: vert1[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[normalPos]  : vert1[normalPos])-1);
						
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[0] 			: vert4[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[textPos] 	: vert4[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[normalPos]  : vert4[normalPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[0] 			: vert3[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[textPos] 	: vert3[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[normalPos]  : vert3[normalPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert4[0] 			: vert1[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert4[textPos] 	: vert1[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert4[normalPos]  : vert1[normalPos])-1);
					} else {
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[0] 			: vert3[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[textPos] 	: vert3[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert1[normalPos]  : vert3[normalPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[0] 			: vert2[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[textPos] 	: vert2[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert2[normalPos]  : vert2[normalPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[0] 			: vert1[0])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[textPos] 	: vert1[textPos])-1);
						faceIndices.add(Integer.parseInt(REVERSE_FACE_ORDER ? vert3[normalPos]  : vert1[normalPos])-1);
					}
				}
			}
			
			//Face Building
			List<Float> finalVertices = faceBuilder(positions, textCoords, normals, faceIndices,
					!noTextureCoords, !noNormals);
			Integer[] indices = new Integer[finalVertices.size() / 8];
			for(int i = 0; i < indices.length / 8; i++)
				indices[i] = i;

			currentModel = new OBJModel(finalVertices.toArray(new Float[0]), indices);

			//currentModel = new OBJModel(positions.toArray(new Float[0]), textCoords.toArray(new Float[0]),
			//normals.toArray(new Float[0]), indices.toArray(new Integer[0]));

			models.put(new String(currentName), (OBJModel)currentModel.clone());
				
			return models;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static final List<Float> faceBuilder(List<Float> verts, List<Float> texts, List<Float> norms, List<Integer> indices,
			boolean textureCoords, boolean normals) {
		List<Float> output = new ArrayList<Float>();
		for (int i = 0; i < indices.size(); i += 3) {
			output.add(verts.get(((indices.get(i+0))*3)+0));
			output.add(verts.get(((indices.get(i+0))*3)+1));
			output.add(verts.get(((indices.get(i+0))*3)+2));
			
			if (textureCoords) {
				output.add(texts.get(((indices.get(i+1))*2)+0));
				output.add(texts.get(((indices.get(i+1))*2)+1));
			} else {
				output.add(verts.get(((indices.get(i+0))*3)+0));
				output.add(verts.get(((indices.get(i+0))*3)+1));
			}
			
			if (normals) {
				output.add(norms.get(((indices.get(i+2))*3)+0));
				output.add(norms.get(((indices.get(i+2))*3)+1));
				output.add(norms.get(((indices.get(i+2))*3)+2));
			} else {
				//TODO calculate normals!
				output.add(0.f);
				output.add(1.f);
				output.add(0.f);
			}
		}
		return output;
	}
	
}
