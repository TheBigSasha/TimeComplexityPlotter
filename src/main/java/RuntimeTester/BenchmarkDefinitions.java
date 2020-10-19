package RuntimeTester;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BenchmarkDefinitions {
    private static int speed = 0;
    private static RandomTweets rand;
    private static int seed = 24601;

    public static double getAdjustmentFactor() {
        return adjustmentFactor;
    }

    public static void setAdjustmentFactor(double adjustmentFactor) {
        BenchmarkDefinitions.adjustmentFactor = adjustmentFactor;
    }

    private static double adjustmentFactor = 1d;

    public static int getSimulationSpeed(){
        return (int) Math.round((double) speed * adjustmentFactor);
    }

    private static void determineSpeed(){
        if(speed != 0) return;
        ArrayList<Integer> runs = new ArrayList<>();
        int[] testCase = new int[]{325325, 532626};
        for(int i = 0; i <50; i++) {
            long startTime = System.nanoTime();
            Arrays.sort(testCase);
            long endTime = System.nanoTime();
            runs.add((int) (endTime - startTime));
        }

        for(int i = 0; i <30; i++) {
            runs.add((int) (arraysSort(2) / 14));
        }
        runs.remove(0); runs.remove(1);
        OptionalDouble avg = runs.stream().mapToInt(a -> a).average();
        if(avg.isPresent()) {
            speed = (int) Math.round(avg.getAsDouble());
        }
        if(speed == 0){
            speed = 1000;
        }
        System.out.println("Slowness of your computer is " + speed);
    }

    public BenchmarkDefinitions(){
        if(rand == null){
            rand = new RandomTweets(seed);
        }
        determineSpeed();
    }

    @benchmark(name = "traverse list", category = "math demos", expectedEfficiency = "O(N)")
    public Long testPop(Long size){
        //System.out.println("Invoked benchmark for size " + size);
        return size * getSimulationSpeed();
    }

    @benchmark(name = "sort", expectedEfficiency = "o(n^2)")
    public Long slowSort(Long size){
        return size * size * getSimulationSpeed();
    }

    @benchmark(name = "fast sorting ", description = "lol kek", expectedEfficiency = "o(n log(n))")
    public long fastSort(long size){
        return (int) (size * Math.log(size)) * getSimulationSpeed();
    }

    @benchmark(name="superfast", description =" this one is sanic fast", expectedEfficiency = "O(1)")
    public long oOfOne(long size){
        return getSimulationSpeed();
    }

    @benchmark(name="java::ArrayList.sort", expectedEfficiency = "O(n log(n)")
    public static long arraysSort(long size){
        ArrayList<Date> dataset = new ArrayList<>();
        for(long i = 0; i < size; i++){
            dataset.add(rand.nextDate());
        }
        long startTime = System.nanoTime();
        dataset.sort(Date::compareTo);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    @benchmark(name="java::queue.deqeue", expectedEfficiency = "O(1)")
    public long enqueueTest(long size){
        Queue<Date> dataset = new ConcurrentLinkedQueue<>();
        for(long i = 0; i < size; i++){
            dataset.add(rand.nextDate());
        }
        long startTime = System.nanoTime();
        dataset.poll();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    @benchmark(name="java::queue.get from middle", expectedEfficiency = "O(N)")
    public long getFromMiddleOfQueue(long size){
        Queue<Date> dataset = new ConcurrentLinkedQueue<>();
        for(long i = 0; i < size; i++){
            dataset.add(rand.nextDate());
        }
        long startTime = System.nanoTime();
        for(int i = 0; i < size/2 ; i++){
            dataset.poll();
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    //TODO: assignment methods can be added here by writing methods with @benchmark annotations
}
