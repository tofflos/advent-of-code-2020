package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var keys = Files.readString(Paths.get("25.in")).lines()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        var publicKey1 = keys.get(0);
        var publicKey2 = keys.get(1);
        var loopSize1 = loopsize(7, publicKey1);
        var encryptionKey = transform(publicKey2, loopSize1);
        
        System.out.println("Part 1: " + encryptionKey);
    }
    
    static int loopsize(int subject, int publicKey) {
        var loopSize = 1;
        var value = 1;
        
        while(true) {
            value = value * subject;
            value = value % 20201227;
            
            if(value == publicKey) {
                break;
            }
            
            loopSize++;
        }
        
        return loopSize;
    }
    
    static long transform(int subject, int loopSize) {
        var value = 1L;

        for(int i=0; i<loopSize; i++) {
            value = value * subject;
            value = value % 20201227;
        }
        
        return value;
    }
}