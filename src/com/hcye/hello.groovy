package com.hcye


def init(String aaa){
    this.aaa=aaa
    return this
}

def bb(){
    println ("this is the second method" + this.aaa)
    return this
}

def cc(){
    println("this is the third method" + this.aaa)
}