package pl.android.puzzledepartment;

/**
 * Created by Maciek Ruszczyk on 2017-12-27.
 */

public enum LoadGameMode {
    NEW(0),
    LOAD(1);

    LoadGameMode(int number) {
        this.number = number;
    }

    private int number;

    public static LoadGameMode fromIntValue(int mode) {
        for(LoadGameMode l:LoadGameMode.values()){
            if(l.number == mode)
                return l;
        }
        return null;
    }

    public int toInt() {
        return number;
    }
}
