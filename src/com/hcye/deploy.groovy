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

//        def endTime = TimeCategory.plus(new Date(), TimeCategory.getMinutes(timeoutMinutes, 5))  // 5minute timeout
    int counter=0
    for(int i=0;i++;i<10){
        if(this.isDeploymentReady()){
            counter +=1
            print(counter)
            sleep(1000)
        }
        if (counter==5){
            String CU_NAME=env.STAGE_NAME+"_deploy_check"
            updateGitlabCommitStatus(name: env.STAGE_NAME, state: 'success')
            return true
        }
    }
    return false
}
//        while (true) {
//            if (new Date() >= endTime) {
//                //超时了，则宣告pod状态不对
//                updateGitlabCommitStatus(name: 'deploy', state: 'failed')
//                throw new Exception("deployment timed out...")
//            }
//            //循环检测当前deployment下的pod的状态
//            try {
//                if (this.isDeploymentReady()) {
//                    readyCount++
//                    if (readyCount > 5) {
//                        updateGitlabCommitStatus(name: 'deploy', state: 'success')
//                        break;
//                    }
//                } else {
//                    readyCount = 0
//                }
//                //每次检测若不满足所有pod均正常，则sleep 5秒钟后继续检测
//                sleep(5)
//            } catch (Exception exc) {
//                echo exc.toString()
//            }
//        }

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
//    apiVersion: apps/v1
//    kind: Deployment
//    metadata:
//    generation: 2
//    labels:
//    app: eladmin
//    name: eladmin
//    namespace: eladmin

}
def getNS(){
    sh "cat "
}