

import se.miun.*;



public class ThreadedUsers{
    static private final int numberOfUsers = 5;
    public static void main(String[] args) {

        WindowProgram windowProgram = new WindowProgram();
        for(int i = 1; i < numberOfUsers; i++)
        {
            Program program = new Program();
        }
    }

}