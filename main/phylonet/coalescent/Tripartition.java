package phylonet.coalescent;

import phylonet.tree.model.sti.STITreeCluster;
import phylonet.tree.model.sti.STITreeCluster.Vertex;
import phylonet.util.BitSet;

public class Tripartition {
	
	STITreeCluster cluster1;
	STITreeCluster cluster2;	
	STITreeCluster cluster3;
	private int _hash = 0;
	
	public Tripartition(STITreeCluster c1, STITreeCluster c2, STITreeCluster c3) {
		
		if (c1 == null || c2 == null || c3 == null) {
			throw new RuntimeException("none cluster" +c1+" "+c2+" "+c3);
		}
		
		int n1 = c1.getBitSet().nextSetBit(0), n2 = c2.getBitSet().nextSetBit(0), n3 = c3.getBitSet().nextSetBit(0);
		if (n1 > n2 & n2 > n3) {
			cluster1 = c1;
			cluster2 = c2;
			cluster3 = c3;
		} else if (n1 > n3 & n3 > n2)  {
			cluster1 = c1;
			cluster2 = c3;	
			cluster3 = c2;
		} else if (n2 > n1 & n1 > n3)  {
			cluster1 = c2;
			cluster2 = c1;	
			cluster3 = c3;
		} else if (n2 > n3 & n3 > n1)  {
			cluster1 = c2;
			cluster2 = c3;	
			cluster3 = c1;
		} else if (n3 > n1 & n1 > n2)  {
			cluster1 = c3;
			cluster2 = c1;	
			cluster3 = c2;
		} else if (n3 > n2 & n2 > n1)  {
			cluster1 = c3;
			cluster2 = c1;	
			cluster3 = c2;
		} else {
			throw new RuntimeException("taxa appear multiple times?\n"+c1+"\n"+c2+"\n"+c3);
		}
	}
	@Override
	public boolean equals(Object obj) {
		Tripartition trip = (Tripartition) obj; 
		
		return this == obj ||
				((trip.cluster1.equals(this.cluster1) && trip.cluster2.equals(this.cluster2) && trip.cluster3.equals(this.cluster3)));					
	}
	@Override
	public int hashCode() {
		if (_hash == 0) {
			_hash = cluster1.hashCode() * cluster2.hashCode() * cluster3.hashCode();
		}
		return _hash;
	}
	@Override
	public String toString() {		
		return cluster1.toString()+"|"+cluster2.toString()+"|"+cluster3.toString();
	}
	
	private int F(int a,int b,int c) {
		return a*b*c*(a+b+c-3);
	}
	
	
	public int sharedQuartetCount(Tripartition other) {
		
		//int [] I = new int [9];
		int 
		I0 = cluster1.getBitSet().intersectionSize(other.cluster1.getBitSet()),
		I1 = cluster1.getBitSet().intersectionSize(other.cluster2.getBitSet()),
		I2 = cluster1.getBitSet().intersectionSize(other.cluster3.getBitSet()),
		I3 = cluster2.getBitSet().intersectionSize(other.cluster1.getBitSet()),
		I4 = cluster2.getBitSet().intersectionSize(other.cluster2.getBitSet()),
		I5 = cluster2.getBitSet().intersectionSize(other.cluster3.getBitSet()),
		I6 = cluster3.getBitSet().intersectionSize(other.cluster1.getBitSet()),
		I7 = cluster3.getBitSet().intersectionSize(other.cluster2.getBitSet()),
		I8 = cluster3.getBitSet().intersectionSize(other.cluster3.getBitSet());
		
		int a= F(I0,I4,I8)+F(I0,I5,I7)+
				F(I1,I3,I8)+F(I1,I5,I6)+
				F(I2,I3,I7)+F(I2,I4,I6); 
		//System.err.println(a);
		return a;
	}


}