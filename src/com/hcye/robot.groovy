package com.hcye

def init(projectName){
    this.projectName=projectName
    return this
}

def helloworld(){
    echo "${this.projectName}! hello world"
}

