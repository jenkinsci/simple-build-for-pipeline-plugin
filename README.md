# Jenkinsfile - making the easy things easy

![Great](great.jpg)

This repo aims (work in progress) to build out a DSL for Jenkinfile (pipeline as code) to make the easy things easy, without taking away any of the power of Jenkins Pipeline for when you really need it. 


# Example

This example runs a very simple build with some environment variables. This would work with pipeline-as-code and branch source.

This should be familiar and declarative-ish looking to anyone familiar with tools like .travis.yml, however, it is implemented as a pipeline DSL (so you can do all the other powerful pipeline things). 

```
simpleBuild {
 
    env = [
        FOO : 42,
        BAR : "YASS"
    ]
    

    before_script = "echo before"
    script = 'echo after $FOO'
    
    
    notifications = [
        email : "mneale@cloudbees.com"    
    ]
    
    
}
```

It will do the right thing, and send emails when things break. 

Optional things: You can specify a machine label, and/or a docker image to run builds under: 

```
machine = "hi-speed"
docker_image = "java:1.9"
```

See [simpleBuild.groovy](simpleBuild.groovy) for the details.

Work in progress: especially around whitelist operation in SCM. 

Needs whitelisting: 
method java.util.Map$Entry getKey
method java.util.Map$Entry getValue
