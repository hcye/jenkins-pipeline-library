package com.hcye

def init(parms){
    this.parms=parms
    return this
}

def start() {
    try{
        echo "Trigger to execute Acceptance Testing"
        def rf = build job: 'robot',    //project name
                parameters: [
                        string(name: 'params', value: this.parms)   //
                ],
                wait: true,
                propagate: false
        def result = rf.getResult()
        def msg = "${env.STAGE_NAME}... "
        if (result == "SUCCESS"){
            msg += "√ success"
        }else if(result == "UNSTABLE"){
            msg += "⚠ unstable"
        }else{
            msg += "× failure"
        }
        echo rf.getAbsoluteUrl()
        env.ROBOT_TEST_URL = rf.getAbsoluteUrl()
//        new BuildMessage().updateBuildMessage(env.BUILD_TASKS, msg)
    } catch (Exception exc) {
        echo "trigger  execute Acceptance Testing exception: ${exc}"
//        new BuildMessage().updateBuildMessage(env.BUILD_RESULT, msg)
    }
}

def helloworld(){
    echo "${this.parms}! hello world"
}

