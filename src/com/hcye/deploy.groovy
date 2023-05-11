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
        sh "kubectl apply -f ${this.resourcePath}/"
        updateGitlabCommitStatus(name: env.STAGE_NAME, state: 'success')
//        this.msg.updateBuildMessage(env.BUILD_TASKS, "${env.stage_name} OK...  √")
        return this
    } catch (Exception exc){
        updateGitlabCommitStatus(name: env.STAGE_NAME, state: 'failed')
//        this.msg.updateBuildMessage(env.BUILD_TASKS, "${env.stage_name} failed...  √")
        throw exc
    }
}

def check() {
    echo "start check"
    int counter=0
    int i=10
    while (i>0){
        echo "into cycle"
        if(this.isDeploymentReady()){
            counter +=1;
            print(counter);
            sleep(5);
        }
        if (counter==5){
            String CU_NAME=env.STAGE_NAME+"_deploy_check";
            updateGitlabCommitStatus(name: env.STAGE_NAME, state: 'success');
            echo 'check success!';
            return true;

        }
        i -=1;
    }
    echo 'check false!';
    return false;
}

def isDeploymentReady(){
    sh "kubectl get -f ${this.resourcePath}/ | grep -v READY > status"
    String datas = readFile "status"
//    NAME      READY   UP-TO-DATE   AVAILABLE   AGE
//    eladmin   3/3     3            3           26d
    List<String> dts= datas.split(' ')
    int counter=0;
    for(String i in dts){
        if(i.strip().length()>0){
            counter +=1;
        }
        if (counter == 2){
            i_s=i.split('/')
            if (i_s[0].equals(i_s[1])) {
                print("true")
                return true
            }else {
                print("false" )
                return false}

        }
    }
}
