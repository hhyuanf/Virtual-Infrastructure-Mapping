package vi_mapping;

public class Virtual_Request {
	public int numofvn = 0;
	public int numofvl = 0;
	Virtual_Node[] VNode = new Virtual_Node[Mapping.PHY_NODE];
	Virtual_Link[] VLink = new Virtual_Link[Mapping.PYH_LINK];
	public int success = 1;
	
	public void TrafficGenerator() {
		this.numofvn = Mapping.uni_rv(Mapping.MAX_VNode, 2);
		for (int i = 0; i < this.numofvn; i++) {
			this.VNode[i].compute = (int)Mapping.uni_rv() * Virtual_Node.COM_DEMAND;
			this.VNode[i].memory = (int)Mapping.uni_rv() * Virtual_Node.MEM_DEMAND;
			this.VNode[i].storage = (int)Mapping.uni_rv() * Virtual_Node.STO_DEMAND;
		}
		if (this.numofvn == 2) {
			this.numofvl = 1;
			this.VLink[0].source = 0;
			this.VLink[0].destination = 1;
			this.VLink[0].bandwidth = Virtual_Link.Bandwidth.values()[Mapping.uni_rv(Mapping.MAX_VLBAND, 0)].value; 
		}
		else {
			this.numofvl = this.numofvn;
			for (int i = 0; i < this.numofvl-1; i++) {
				this.VLink[i].source = i;
				this.VLink[i].destination = i+1;
				this.VLink[i].bandwidth = Virtual_Link.Bandwidth.values()[Mapping.uni_rv(Mapping.MAX_VLBAND, 0)].value;
			}
			this.VLink[this.numofvl-1].source = this.numofvl-1;
			this.VLink[this.numofvl-1].destination = 0;
			this.VLink[this.numofvl-1].bandwidth = Virtual_Link.Bandwidth.values()[Mapping.uni_rv(Mapping.MAX_VLBAND, 0)].value; 
		}
	}
}
