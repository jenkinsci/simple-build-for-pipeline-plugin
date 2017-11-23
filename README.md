# Jenkinsfile - making the easy things easy

This plugin aims to build out a DSL for Jenkinfile (pipeline as code) to make the easy things easy, without taking away any of the power of Jenkins Pipeline for when you really need it.


[![Build Status](https://jenkins.ci.cloudbees.com/job/plugins/job/simple-build-for-pipeline-plugin/badge/icon)](https://jenkins.ci.cloudbees.com/job/plugins/job/simple-build-for-pipeline-plugin/)

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
 The only code you need to care about is the `src/main/resources/dslsimpleBuild.groovy` and `SimpleBuildDSL.java` - that is all.
 
 # TODO
- [ ] More scenario tests of the permutations
- [ ] Use named stages (?) if it makes sense. 
- [ ] Support all the same _script semantics as https://github.com/jenkinsci/simple-travis-runner-plugin/blob/pure-script-approach/README.md (eg after_failure, after_success etc to happen on the appropriate events and around error handling). 
- [ ] Allow execution of all `script` entries even if one fails (?)
- [ ] Time out individual `script` entries.
- [ ] Add in simple matrix (a la travis)
- [ ] Add in branch inclusions/exclusions (what branches to run on or not)
- [ ] Allow parallel scripts (across nodes?? or a whole block of parallel?)
- [ ] Implement auto-generation of `script` and friends for (some) languages? Debatable whether to do this.
- [ ] Decide whether to keep emulating Travis's behavior of ignore `after_*` steps' failures when setting build status.
- [ ] allow block for simple user input
- [ ] More notification types (slack, for example)
- [ ] Change email to only be on failure or return to health (convention, Yo). 
- [ ] Retries of scripts if specified
- [ ] May need to configure docker to work under a node() block... 


