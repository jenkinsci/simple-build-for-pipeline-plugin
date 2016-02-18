# Jenkinsfile - making the easy things easy

![Great](great.jpg)

This plugin aims to build out a DSL for Jenkinfile (pipeline as code) to make the easy things easy, without taking away any of the power of Jenkins Pipeline for when you really need it.


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

See src/main/resources for the actual DSL used for the details.

To play with this:

`mvn hpi:run`

 or build and install it to your Jenkins (it is just a plugin). You can also commit the simpleBuild.groovy to the Jenkins workflowLibs repo (if you know what I mean, if not... then ignore me!).

 # Secondary aim

 The secondary aim of this plugin is to show how plain pipeline-script can be used easily to make a plugin to allow you to share the DSL.
 The only code you need to care about is the `simpleBuild.groovy` and `SimpleBuildDSL` - that is all.


