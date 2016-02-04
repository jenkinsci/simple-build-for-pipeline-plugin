# Jenkinsfile - making the easy things easy

![Great](great.jpg)

This repo aims to build out a DSL for Jenkinfile (pipeline as code) to make the easy things easy (and the hard things doable, as they are already).


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

Optional things: You can specify a machine label (where build runs) and (soon) a docker image to use as an alternative environment. 
