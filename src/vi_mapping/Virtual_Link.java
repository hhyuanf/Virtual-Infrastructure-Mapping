package vi_mapping;
import java.util.Arrays;

public class Virtual_Link {
	
	//public static final int BAND_DEMAND = 4; // 0->10Gbps, 1->40Gbps, 2->100Gbps, 3->200Gbps, 4->400Gbps, 5->1Tbps
	public static enum Bandwidth {
		TenG(10), FourtyG(40), OneHG(100), TwoHG(200), FourHG(400), OneTG(1000);
		public final int value;
    	private Bandwidth(int value) {
            this.value = value;
    	}
    	public int getBandwidth()
        {
            return value;
        }
};

	
	public int source = -1;
	public int destination = -1;
	public int bandwidth = 0;

	public int[][] route = new int[Mapping.K_PATH][Mapping.PHY_NODE];
	public int[] hop = new int[Mapping.K_PATH];
	public int[] distance = new int[Mapping.K_PATH];
	public double[] modulation = new double[Mapping.K_PATH];
	public int[] start = new int[Mapping.K_PATH];
	public int[] width = new int[Mapping.K_PATH];
	public double[] p = new double[Mapping.K_PATH]; // link mapping probability

	public int map = -1;
	
	public Virtual_Link() {
		for (int[] row:route) {
			Arrays.fill(row, -1);
		}
			
		Arrays.fill(start, -1);
	}
}
