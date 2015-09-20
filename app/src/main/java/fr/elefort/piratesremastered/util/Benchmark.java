package fr.elefort.piratesremastered.util;

import java.util.LinkedList;

public class Benchmark {
	private final LinkedList<Long> times = new LinkedList<>();
	private final static int MAX_SIZE = 100;
	private final double NANOS = 1000000000.0;
	
	/** Calculates and returns frames per second */
	
	public Benchmark() {
		times.add(System.nanoTime());
	}
	
	public double fps() {
	    long lastTime = System.nanoTime();
	    double difference = (lastTime - times.getFirst()) / NANOS;
	    times.addLast(lastTime);
	    int size = times.size();
	    if (size > MAX_SIZE) {
	        times.removeFirst();
	    }
	    return difference > 0 ? times.size() / difference : 0.0;
	}
	
	public double ram(){
		return (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
	}
	
	public double ramAllocate(){
		return (double)(Runtime.getRuntime().totalMemory()) / 1048576L;
	}
}

