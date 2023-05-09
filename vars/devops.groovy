import com.hcye.build
def docker(String repo, String tag, String credentialsId, String dockerfile="Dockerfile", String context="."){
    return new build().init( repo,  tag,  credentialsId,  dockerfile="Dockerfile",  context=".")
}


docker('harbor.synsense-neuromorphic.com/local/ci-tools:withsonar','latest','dockerid').docker_login()