package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var foods = Files.readString(Paths.get("21.in")).lines().map(Food::of).collect(Collectors.toList());
        var allergens = foods.stream().flatMap(f -> f.allergens().stream()).collect(Collectors.toSet());
        var candidates = new HashMap<String, List<Set<String>>>();

        for (var allergen : allergens) {
            for (var food : foods) {
                if (food.allergens().contains(allergen)) {
                    candidates.putIfAbsent(allergen, new ArrayList<>());
                    candidates.get(allergen).add(food.ingredients());
                }
            }
        }
        
        var active = candidates.entrySet().stream()
                .flatMap(e -> e.getValue().stream().reduce(Main::intersection).stream())
                .collect(Collectors.toSet()).stream()
                .flatMap(Set::stream).collect(Collectors.toSet());


        var count = foods.stream().flatMap(f -> f.ingredients().stream()).filter(i -> !active.contains(i)).count();
        
        System.out.println("Part 1: " + count);

        var pairs = new ArrayList<Pair>();
        
        for(var entry : candidates.entrySet()) {
           var allergen = entry.getKey(); 
           var ingredients = entry.getValue().stream().reduce(Main::intersection).orElseThrow();
           pairs.add(new Pair(allergen, ingredients));
        }
        
        var identified = new ArrayList<>();
        
        while(pairs.stream().anyMatch(l -> l.ingredients().size() > 1)) {
            
            for(var pair : pairs) {
                if(pair.ingredients().size() == 1 && !identified.contains(pair.ingredients().stream().findAny().orElseThrow())) {
                    identified.add(pair.ingredients().stream().findAny().orElseThrow());
                }
                
                if(pair.ingredients().size() > 1) {
                    pair.ingredients().removeAll(identified);
                }
            }
        }
        
        System.out.println("Part 2: " + pairs.stream().sorted((p1, p2) -> p1.allergen().compareTo(p2.allergen()))
                .map(p -> p.ingredients().stream().findAny().orElseThrow())
                .collect(Collectors.joining(",")));
    }

    static <T> Set<T> intersection(Set<T> s1, Set<T> s2) {
        var result = new HashSet<T>(s1);
        result.retainAll(s2);
        return result;
    }
}

record Food(Set<String> ingredients, Set<String> allergens) {

    static Food of(String s) {
        return new Food(Arrays.stream(s.substring(0, s.indexOf("(") - 1).split(" ")).collect(Collectors.toSet()),
                Arrays.stream(s.substring(s.indexOf("contains ") + "contains ".length(), s.length() - 1).split(", ")).collect(Collectors.toSet())
        );
    }
}

record Pair(String allergen, Set<String> ingredients) {
    
}