package vi_mapping;

//import java.util.Arrays;
import java.util.Random;

public class Mapping {
	
	public static final int K_PATH = 1;
	public static final int MAX_VNode = 5;
	public static final int MAX_VLBAND = 5;
	
	public static final int PYH_LINK = 21;
	public static final int PHY_NODE = 14; //NSFNET
	public static final double BPSK = 1.6;
	public static final double QPSK = 3.2;
	public static final double QAM = 6.4;
	public static final double SLOT = 12.5;

	public static int uni_rv(int max, int min) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
	
	public static double uni_rv() {
		Random random = new Random();
		return random.nextDouble();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void Modulation(Physical_Link[][] phy, Virtual_Request req, int link) {
		for (int k = 0; k < K_PATH; k++) {
			for (int i = 0; i < req.VLink[link].hop[k] - 1; i++) {
				req.VLink[link].distance[k] = req.VLink[link].distance[k] + phy[req.VLink[link].route[k][i]][req.VLink[link].route[k][i+1]].distance;
			}
			if (req.VLink[link].distance[k] <= 1000)
				req.VLink[link].modulation[k] = QAM;
			else if (req.VLink[link].distance[k] > 1000 && req.VLink[link].distance[k] <= 3000)
				req.VLink[link].modulation[k] = QPSK;
			else if (req.VLink[link].distance[k] > 3000 && req.VLink[link].distance[k] <= 8000) 
				req.VLink[link].modulation[k] = BPSK;
			else 
				req.VLink[link].modulation[k] = 1;
			req.VLink[link].width[k] = (int)Math.ceil(req.VLink[link].bandwidth/req.VLink[link].modulation[k]/SLOT);
		}
	}
	
	public int SpecAllocation(Physical_Link[][] phy, Virtual_Request req, int link) {
		int success = 1;
		double max_prob = 0;
		for (int k = 0; k < K_PATH; k++) {
			int flag = 1;
			int[] bitmap = new int[Physical_Link.SPECTRUM_CAPACITY];
			for (int i = 0; i < Physical_Link.SPECTRUM_CAPACITY; i++) {
				for (int j = 0; j < req.VLink[link].hop[i] - 1; j++) {
					if(phy[req.VLink[link].route[k][j]][req.VLink[link].route[k][j+1]].spectrum[i]==1)
						bitmap[i] = 1;
				}
			}
			for(int i = 0; i < Physical_Link.SPECTRUM_CAPACITY; i++)
			{
				int available_slot = 0;
				for(int j = i; j < i + req.VLink[link].width[k]; j++)
					available_slot = available_slot + bitmap[j];
				if(available_slot == 0)
				{
					req.VLink[link].p[k] = req.VLink[link].p[k] + 1;
					if(flag == 1)
					{
						req.VLink[link].start[k] = i;
						flag = 0;
					}
				}
			}
			req.VLink[link].p[k] = req.VLink[link].p[k] / (Physical_Link.SPECTRUM_CAPACITY - req.VLink[link].width[k] + 1);	
			// choose the path with the max link mapping probability
			if(req.VLink[link].p[k] > max_prob)
			{
				req.VLink[link].map = k;
				max_prob = req.VLink[link].p[k];
			}		
		}
		if(max_prob == 0)
			success = 0;
		
		return success;
	}
	
	public int VLMapping(Physical_Link[][] phy_l, Physical_Node[] phy_n, Virtual_Request req) {
		int success = 1;
		for (int i = 0; i < req.numofvl; i++) {
			//Routing
			Modulation(phy_l, req, i);
			if (SpecAllocation(phy_l, req, i) > 1) {
				Physical_Link.ReserveSource(phy_l, req, i);
			}
			else if (SpecAllocation(phy_l, req, i) == 0) {
				success = 0;
				Physical_Node.ReleaseSource(phy_n, req);
				Physical_Link.ReleaseSource(phy_l, req);
			}
		}
		req.success = success;
		return success;
	}
	
	public void Display (Virtual_Request req, int num) {
		System.out.println("###########################################");
		System.out.println("Virtual Request #" + num + ", with " + req.numofvn + " virtual nodes, and " + req.numofvl + " virtual links");
		if (req.success == 1) {
			System.out.println("-------------------------------------------");
			System.out.println("virtual node mapping");
			for (int i = 0; i < req.numofvn; i++) {
				System.out.println("vn" + i + "->pn" + req.VNode[i].map + ", " + req.VNode[i].compute + "(compute)" + ", " + req.VNode[i].storage + "(storage)" + ", " + req.VNode[i].memory + "(memory)");
			}
			System.out.println("-------------------------------------------");
			System.out.println("virtual link mapping");
			for(int i = 0; i < req.numofvl;i++) {
				System.out.println("vl" + i + "(" + req.VLink[i].source + ", " + req.VLink[i].destination + "), " + req.VLink[i].bandwidth + " Gbps");

				for(int j = 0; j < req.VLink[i].hop[req.VLink[i].map]; j++)
					System.out.println(req.VLink[i].route[req.VLink[i].map][j] + "->" );
				System.out.println("start form " + req.VLink[i].start[req.VLink[i].map] + ", with width" + req.VLink[i].width[req.VLink[i].map]);
			}
		}
		else {
			System.out.println("The request is BLOCKED!!!!!");
		}
		System.out.println("###########################################");
	}
}
