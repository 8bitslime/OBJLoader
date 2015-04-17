package net.slimeco.objloader;

public class OBJVector3f {
	
	public float x;
	public float y;
	public float z;	
	
	public OBJVector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	public OBJVector3f(float x) {
		this.x = x;
	}
	public OBJVector3f(float x, float y) {
		this.x = x;
		this.x = y;
	}
	public OBJVector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public OBJVector3f clone() {
		return new OBJVector3f(x, y, z);
	}
	
	public String toString() {
		return x + " " + y + " " + z;
	}
}
