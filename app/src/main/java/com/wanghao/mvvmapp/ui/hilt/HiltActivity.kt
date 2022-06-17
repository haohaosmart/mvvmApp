package com.wanghao.mvvmapp.ui.hilt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wanghao.mvvmapp.R
import com.wanghao.mvvmapp.databinding.ActivityMainBinding



class HiltActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



      /*
        编译不通过
        val animals = ArrayList<Animal>()
        val dogs = ArrayList<Dog>()
        animals = dogs*/
        // dog是Animal的子类型     animalPrinter是 dogPrinter的子类型？

        val animalPrinter = AnimalPrinter()
        animalPrinter.print(Animal())

        val dogPrinter = DogPrinter()
        dogPrinter.print(Dog())

//
//        dogPrinter = animalPrinter

    }

}


open class Animal{}


open class Dog: Animal(){}


abstract  class  Printer<in E>{
    abstract fun print(value: E): Unit
}


class AnimalPrinter: Printer<Animal>() {
    override fun print(value: Animal) {
        print("This is animal")
    }
}

class DogPrinter: Printer<Dog>(){
    override fun print(value:Dog) {
        print("This is dog")
    }
}