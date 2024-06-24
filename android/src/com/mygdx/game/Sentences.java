package com.mygdx.game;

import java.util.ArrayList;

public class Sentences {
    ArrayList<String> commands = new ArrayList<>();

    public Sentences(){
        // Adding command-like sentences to the list in the constructor
        commands.add("Yes");
        commands.add("No");
        commands.add("Can you repeat that");
        commands.add("Why");
        commands.add("Who are you");
        commands.add("When");
        commands.add("Damn you");
        commands.add("Can you be quiet");
        commands.add("Can you say something");
        commands.add("I agree with that");
    }
}
