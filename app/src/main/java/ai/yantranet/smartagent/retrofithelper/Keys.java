package ai.yantranet.smartagent.retrofithelper;

public class Keys {

    private static native String getFirstKey();

    private static native String getSecondKey();

    public static String readFirstKey() {
        return getFirstKey();
    }

    public static String readSecondKey() {
        return getSecondKey();
    }



}
