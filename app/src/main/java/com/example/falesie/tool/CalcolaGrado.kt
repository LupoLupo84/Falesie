package com.example.falesie.tool

fun calcolaGrado(grado: String): Int {
    var risultato = 0;
    when (grado) {
        "nl" -> risultato = -1
        "3a" -> risultato = 1
        "3a+" -> risultato = 2
        "3b" -> risultato = 3
        "3b+" -> risultato = 4
        "3c" -> risultato = 5
        "3c+" -> risultato = 6
        "4a" -> risultato = 7
        "4a+" -> risultato = 8
        "4b" -> risultato = 9
        "4b+" -> risultato = 10
        "4c" -> risultato = 11
        "4c+" -> risultato = 12
        "5a" -> risultato = 13
        "5a+" -> risultato = 14
        "5b" -> risultato = 15
        "5b+" -> risultato = 16
        "5c" -> risultato = 17
        "5c+" -> risultato = 18
        "6a" -> risultato = 19
        "6a+" -> risultato = 20
        "6b" -> risultato = 21
        "6b+" -> risultato = 22
        "6c" -> risultato = 23
        "6c+" -> risultato = 24
        "7a" -> risultato = 25
        "7a+" -> risultato = 26
        "7b" -> risultato = 27
        "7b+" -> risultato = 28
        "7c" -> risultato = 29
        "7c+" -> risultato = 30
        "8a" -> risultato = 31
        "8a+" -> risultato = 32
        "8b" -> risultato = 33
        "8b+" -> risultato = 34
        "8c" -> risultato = 35
        "8c+" -> risultato = 36
        "9a" -> risultato = 37
        "9a+" -> risultato = 38
        "9b" -> risultato = 39
        "9b+" -> risultato = 40
        "9c" -> risultato = 41
        "9c+" -> risultato = 42
    }
    return risultato
}