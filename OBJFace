package net.slimeco.objloader;

public class OBJFace {
	
	public OBJVector3f[] vertices      = new OBJVector3f[3];
	public OBJVector3f[] textureCoords = new OBJVector3f[3];
	public OBJVector3f[] normals       = new OBJVector3f[3];
	
	public OBJFace() {}
	public OBJFace(OBJVector3f[] vertices, OBJVector3f[] textCoords, OBJVector3f[] normals) {
		if (vertices.length == 3)
			this.vertices = vertices.clone();
		if (textCoords.length == 3)
			this.textureCoords = textCoords.clone();
		if (normals.length == 3)
			this.normals = normals.clone();
	}
	
	public String toString() {
		String r = "";
		for (int i = 0; i < 3; i++) {
			r += vertices[i] + " ";
			r += textureCoords[i] + " ";
			r += normals[i] + " ";
		}
		return r;
	}
}
