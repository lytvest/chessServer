package ru.lytvest.chess;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Objects;
import java.util.PriorityQueue;

public class Timers {

    private static float currentTime = 0;
    private static PriorityQueue<Timer> queue = new PriorityQueue<>((o1, o2) -> Float.compare(o1.time, o2.time));


    static void update(float delta){
        currentTime+= delta;
        while (!queue.isEmpty() && queue.peek().time <= currentTime){
            val timer = queue.poll();
            timer.run();
        }
    }

    static void runOneFrom(float seconds, Runnable runnable){
        queue.add(new Timer("one_" + currentTime, currentTime + seconds, runnable));
    }

    static void repeat(String label, float time, Runnable runnable){
        queue.add(new RepeatTimer(label, time, runnable));
    }

    static void stop(String label){
        queue.remove(new Timer(label, 0, null));
    }


    @AllArgsConstructor
    static class Timer {
        String label;
        float time;
        Runnable runnable;

        public void run(){
            runnable.run();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;

            Timer timer = (Timer) o;

            return Objects.equals(label, timer.label);
        }

        @Override
        public int hashCode() {
            return label != null ? label.hashCode() : 0;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    static class RepeatTimer extends Timer{
        float repeatTime;
        public RepeatTimer(String label, float time, Runnable runnable) {
            super(label, time, runnable);
            repeatTime = time;
            this.time = currentTime + repeatTime;
        }

        @Override
        public void run() {
            super.run();
            this.time = currentTime + repeatTime;
            queue.add(this);
        }
    }

    public static void main(String[] args) {
        repeat("ok", 2, () -> System.out.println("repeat ok"));
        update(1);
        update(1);
        update(1);
        update(1);
        update(1);
        update(1);
        stop("ok");
        update(1);
        update(1);
        update(1);
        update(1);
        update(1);

    }
}
