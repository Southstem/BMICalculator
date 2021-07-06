package com.example.recyclerviewtest;

public class item {
    public float height;
    public float weight;
    public double bmi;
    public String time;
    public item(float h, float w, String t){
        height = h;
        weight = w;
        time = t;
        bmi = culBMI();
    }
    public double culBMI()
    {
        double bmiHere = weight * 1.0 / height / height;
        return bmiHere;
    }
}
