package com.hcye

def init(String resourcePath){
    this.resourcePath = resourcePath
//    this.msg = new BuildMessage()
    return this
}


def start(){
    try{
        //env.CURRENT_IMAGE用来存储当前构建的镜像地址，需要在Docker.groovy中设置值
        sh "sed -i 's#{{ELADMIN-IMAGE}}#${env.CURRENT_IMAGE}#g' ${this.resourcePath}/*"
        sh "cat ${this.resourcePath}/eladmin-api.yaml"
        //  CURRENT_IMAGE
        sh "sleep 600"
        def proc = "ls /usr/local/bin/kubectl; kubectl apply -f ${this.resourcePath}/".execute()
        def b = new StringBuffer()
        proc.consumeProcessErrorStream(b)
        println proc.text
        println b.toString()
        updateGitlabCommitStatus(name: env.STAGE_NAME, state: 'success')
//        this.msg.updateBuildMessage(env.BUILD_TASKS, "${env.stage_name} OK...  √")
    } catch (Exception exc){
        updateGitlabCommitStatus(name: env.STAGE_NAME, state: 'failed')
//        this.msg.updateBuildMessage(env.BUILD_TASKS, "${env.stage_name} failed...  √")
        throw exc
    }
}
