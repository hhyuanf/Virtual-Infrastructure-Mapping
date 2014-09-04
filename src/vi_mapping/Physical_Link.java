package vi_mapping;

public class Physical_Link {
	
	public static final int SPECTRUM_CAPACITY = 320;
	
	public int distance;
	public int[] spectrum = new int[SPECTRUM_CAPACITY];
	
	public static void ReserveSource(Physical_Link[][] phy, Virtual_Request req, int link) {
		for (int i = 0; i < req.VLink[link].hop[req.VLink[link].map] - 1; i++) {
			for(int s=req.VLink[link].start[req.VLink[link].map]; s < req.VLink[link].start[req.VLink[link].map]+req.VLink[link].width[req.VLink[link].map]; s++) {
				phy[req.VLink[link].route[req.VLink[link].map][i]][req.VLink[link].route[req.VLink[link].map][i+1]].spectrum[s] = 1;
				phy[req.VLink[link].route[req.VLink[link].map][i+1]][req.VLink[link].route[req.VLink[link].map][i]].spectrum[s] = 1;
			}
		}
	}
	
	public static void ReleaseSource(Physical_Link[][] phy, Virtual_Request req) {
		for (int i = 0; i < req.numofvn; i++) {
			if (req.VLink[i].map != -1) {
				for (int j = 0; j < req.VLink[i].hop[req.VLink[i].map] - 1; j++) {
					for(int s=req.VLink[i].start[req.VLink[i].map]; s < req.VLink[i].start[req.VLink[i].map]+req.VLink[i].width[req.VLink[i].map]; s++) {
						phy[req.VLink[i].route[req.VLink[i].map][j]][req.VLink[i].route[req.VLink[i].map][j+1]].spectrum[s] = 1;
						phy[req.VLink[i].route[req.VLink[i].map][j+1]][req.VLink[i].route[req.VLink[i].map][j]].spectrum[s] = 1;
					}
				}
			}
		}
	}
}
