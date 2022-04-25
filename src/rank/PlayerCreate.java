package rank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import static java.lang.System.in;

public class PlayerCreate {


    public static Player create(int score){
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        System.out.println("Please input your name : ");

        String name = null;

        try {
            name = br.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }

        Date date = new Date();
        return new Player(name, score, date);
    }
}
