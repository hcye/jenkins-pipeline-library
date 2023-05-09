package com.hcye


def init(String repo, String tag, String credentialsId, String dockerfile="Dockerfile", String context="."){
    this.repo=repo
    this.tag=tag
    this.credentialsId=credentialsId
    this.dockerfile=dockerfile
    this.context=context
    this.isLoggedIn=false
    this.fullAddress = "${this.repo}:${this.tag}"
    return this
}

def docker_login(){
    if(this.isLoggedIn || credentialsId == ""){
        return this
    }
    // docker login
    withCredentials([usernamePassword(credentialsId: this.credentialsId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        def regs = this.getRegistry()
        retry(3) {
            try {
                sh "docker login ${regs} -u $USERNAME -p $PASSWORD"
                this.isLoggedIn=true
            } catch (Exception exc) {
                echo "docker login err, " + exc.toString()
            }
        }
    }
    this.isLoggedIn = true;
    return this;

}

def getRegistry(){
    return this.repo.split('/')[0]
}

def push() {
    this.docker_login()
    retry(3) {
        try {
            sh "docker push ${this.fullAddress}"
        }catch (Exception exc) {
            throw exc
        }
    }
    return this
}

def build(){
    this.docker_login()
    retry(3) {   // retry three times
        try {
            sh "docker build ${this.context} -t ${this.fullAddress} -f ${this.dockerfile} "
        }catch (Exception exc) {
            throw exc
        }
        return this
    }
}

