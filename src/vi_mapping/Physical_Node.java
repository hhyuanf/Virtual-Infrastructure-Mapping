package vi_mapping;

public class Physical_Node {
	
	public static final int COMPUTE_CAPACITY = 800;
	public static final int STORAGE_CAPACITY = 800;
	public static final int MEMORY_CAPACITY = 800;
	
	public double compute = COMPUTE_CAPACITY;
	public double storage = STORAGE_CAPACITY;
	public double memory = MEMORY_CAPACITY;
	
	public static void ReserveSource(Physical_Node[] phy, Virtual_Request req, int node) {
		phy[req.VNode[node].map].compute = phy[req.VNode[node].map].compute - req.VNode[node].compute;
		phy[req.VNode[node].map].storage = phy[req.VNode[node].map].storage - req.VNode[node].storage;
		phy[req.VNode[node].map].memory = phy[req.VNode[node].map].memory - req.VNode[node].memory;
	}
	
	public static void ReleaseSource(Physical_Node[] phy, Virtual_Request req) {
		for (int i = 0; i < req.numofvn; i++) {
			if (req.VNode[i].map != -1) {
				phy[req.VNode[i].map].compute = phy[req.VNode[i].map].compute + req.VNode[i].compute;
				phy[req.VNode[i].map].storage = phy[req.VNode[i].map].storage + req.VNode[i].storage;
				phy[req.VNode[i].map].memory = phy[req.VNode[i].map].memory + req.VNode[i].memory;
			}
		}
	}
}
