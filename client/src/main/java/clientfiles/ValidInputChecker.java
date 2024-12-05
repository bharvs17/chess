package clientfiles;

public class ValidInputChecker {

    public static boolean checkRegister(String... params) {
        return params.length == 3;
    }

    public static boolean checkLogin(String... params) {
        return params.length == 2;
    }

    public static boolean checkLogout(String... params) {
        return params.length == 0;
    }

    public static boolean checkReset(String... params) {
        return (params.length == 1 && params[0].equals("admin"));
    }

    public static boolean checkListGames(String... params) {
        return (params.length == 1 && params[0].equals("games"));
    }

    public static boolean checkMakeGame(String... params) {
        return (params.length >= 2 && params[0].equals("game"));
    }

    //implement for play game and all following methods, then commit to github
}
