import com.hcye.*
def docker(String repo, String tag, String credentialsId, String dockerfile="Dockerfile", String context="."){
    return new build().init( repo,  tag,  credentialsId,  dockerfile="Dockerfile",  context=".")
}


def scan(String projectVersion="", Boolean waitScan = true){
    return new sonar().init(projectVersion,waitScan)
}


def apply(resourcePath){
    return new deploy().init(resourcePath)
}

def robot(parms){
    return new robot().init(parms)
}