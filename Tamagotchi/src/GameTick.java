public class GameTick {

    long lastTickTime;
    long elapsedTime;
    int elapsedTicks;
    public GameTick() {

        MainSave.Save save = MainSave.loadSave();

        this.lastTickTime = save.getLastTickTime();
        this.elapsedTime = System.currentTimeMillis() - lastTickTime;
        this.elapsedTicks = (int) (elapsedTime / 10000);

    }

    public void setLastTickTime(long lastTickTime) {
        this.lastTickTime = lastTickTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setElapsedTicks(int elapsedTicks) {
        this.elapsedTicks = elapsedTicks;
    }

    public long getLastTickTime() {
        return lastTickTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public int getElapsedTicks() {
        return elapsedTicks;
    }
}
