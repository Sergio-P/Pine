package cl.uchile.boulder.pine.planner;

public class TimeFreeCounter {

    private int[] usedTimes;

    public TimeFreeCounter(){
        usedTimes = new int[7];
        reset();
    }

    public void reset(){
        for (int i = 0; i < 7; i++) {
            usedTimes[i] = 0;
        }
    }

    public void addBlock(int dow, int dur){
        usedTimes[dow] += dur;
    }

    public int getUsedTime(int dow){
        return usedTimes[dow];
    }

    public int getFreeTime(int dow){
        return 18*60 - getUsedTime(dow);
    }

    public float getProportionFreeTime(int dow){
        return getFreeTime(dow)/(18f*60);
    }

    public boolean hasOvercharge(){
        for (int i = 0; i < 7; i++) {
            if(getProportionFreeTime(i)<0.3f)
                return true;
        }
        return false;
    }

}
