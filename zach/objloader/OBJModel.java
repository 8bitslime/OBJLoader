package zach.objloader;

public class OBJModel {
	
	private float[] vertices;
	private float[] textCoords;
	private float[] normals;
	private int[] indices;
	
	public OBJModel(float[] vertices, float[] textCoords, float[] normals, int[] indices) {
		this.vertices   = vertices;
		this.textCoords = textCoords;
		this.normals    = normals;
		this.indices    = indices;
	}
	
	public OBJModel(Float[] vertices, Float[] textCoords, Float[] normals, Integer[] indices) {
		int pos = 0;
		
		this.vertices = new float[vertices.length];
		for (Float f : vertices)
			this.vertices[pos++] = f;
		
		pos = 0;
		this.textCoords = new float[textCoords.length];
		for (Float f : textCoords)
			this.vertices[pos++] = f;
		
		pos = 0;
		this.normals = new float[normals.length];
		for (Float f : normals)
			this.normals[pos++] = f;
		
		pos = 0;
		this.indices = new int[indices.length];
		for (Integer i : indices)
			this.indices[pos++] = i;
	}
	
	public OBJModel(Float[] vertices, Integer[] indices) {
		int vertexNum  = vertices.length / 8 * 3;
		int textNum    = vertices.length / 8 * 2;
		int normalNum  = vertices.length / 8 * 3;
		
		this.vertices = new float[vertexNum];
		textCoords = new float[textNum];
		normals = new float[normalNum];
		
		int vertexPos = 0;
		int textPos = 0;
		int normalPos = 0;
		for (int i = 0; i < vertices.length; i += 8) {
			this.vertices[vertexPos++] = vertices[i+0];
			this.vertices[vertexPos++] = vertices[i+1];
			this.vertices[vertexPos++] = vertices[i+2];
			
			textCoords[textPos++] = vertices[i+3];
			textCoords[textPos++] = vertices[i+4];
			
			normals[normalPos++] = vertices[i+5];
			normals[normalPos++] = vertices[i+6];
			normals[normalPos++] = vertices[i+7];
		}
		
		int pos = 0;
		this.indices = new int[indices.length];
		for (int i = 0; i < indices.length; i++)
			this.indices[pos++] = indices[i] == null ? i : indices[i];
	}

	@Override
	public Object clone() {
		return new OBJModel(vertices, textCoords, normals, indices);
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextCoords() {
		return textCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}
}
