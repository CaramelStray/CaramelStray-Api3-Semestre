package com.example.demo;

public class Main {


    public static void imprimirSequencia(int n) {
        if (n < 1) {
            return;
        }

        System.out.println(n);
        imprimirSequencia(n- 1);
        System.out.println(n);
    }

    public static void main(String[] args) {

        int n = 5;

        imprimirSequencia(n);
    }
}