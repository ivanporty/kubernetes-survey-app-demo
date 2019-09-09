# Kubernetes Voting App Demo

This repository contains a demo we did to showcase Kubernetes bootstrap, continuous development and then live one-click debugging on the remote K8s cluster with Cloud Code and IntelliJ. I gave the demo during the Cloud Code for Java talk at Devoxx Poland 2019 and Oracle CodeOne 2019 (ex. JavaOne) in SF.

## How to set up the demo

### Step 1 - infrastructure

1. Set up a Kubernetes cluster.

1. Allocate a static external IP to use.

1. (Recommended) Register a domain name. (I used `vote-k8s.org`.)

    1. Add an A record to point to the allocated external IP.
    
1. Clone the repository:

    ```bash
    $ git clone https://github.com/ivanporty/kubernetes-survey-app-demo
    ```

### Step 2 - set up ingress

1. Make sure `kubectl` is configured correctly.

1. Set the `EXTERNAL_IP` environment variable to the allocated external IP:

    ```bash
    export EXTERNAL_IP=???
    ```

1. Set up Ambassador for ingress:

    ```bash
    $ kubectl create clusterrolebinding my-cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud info --format="value(config.account)")
    $ ./apply_ambassador.sh
    ```

### Step 3 - start the app

1. Make sure `kubectl` is configured correctly.

1. Add support with Cloud Code. Install Cloud Code plugin from JetBrains marketplace as usual.

2. Use IJ for frontend-service and voting-service, use WebStorm for notification-service (node.js)

3. Open each microservice separately in an IDE.

4. For frontend-service, use Kubernetes live templates to create standard frontend-service named deployment and service (see _recover folder for example_)

5. For each of microservices, use `Tools` -> `Cloud Code` -> `K8s` -> `Add Kubernetes support`

6. Deploy microservices one by one from IDEs using `Deploy to Kubernetes` run configuration.

1. Once everything is up and running, visit your app at its domain name (or external IP).

1. Results are viewable at `/results`.

### Changing the survey

The survey prompt and options can be changed by editing the HTML files:

`frontend-service/src/main/resources/public/index.html`

- Only need to touch the `#options` `div`.

`frontend-service/src/main/resources/public/results.html`

- Only need to touch the `Votes` `constructor`, and change what `this._votes` is set to.
- Change the domain name in the `#banner` `div`.

## How to give the demo

The following is a script for the demo:

1. State that we have a cluster set up on GKE already, and that it's simple to do so if you don't. All you would need to run is `gcloud container clusters create`.

1. Give a brief overview of the app. Can show a diagram like:

    <img src="assets/survey-app-architecture.png" width="450px" />
    
1. Add standard K8s resources from snippets without any YAML problems, and use Cloud Code to deploy all 3 microservices from IJ/WebStorm, and tail the logs to make sure they are all deployed.


1. Watch as Cloud Code uses Jib/Skaffold and containerizes the 3 microservices and deploys them to the Kubernetes cluster without any extra effort. The app is now live.

1. Navigate to your app's domain to show the audience what they should expect to see. Something like:

    <img src="assets/survey-app-vote-page.png" width="350px" />

1. Navigate to the `/results` page to display the results as they come in. The page will animate in realtime as people cast votes:

    <img src="assets/survey-app-results-page.png" width="350px" />
    
    There is a bug in the voting-service that needs to be fixed (it sends newVotes to notification-service instead of all votes). Live debug it and fix it.

1. Ask the audience to please take out their phones/laptops and navigate to the domain to cast their votes. Watch as the results roll in.

1. Make some comedic remark about the results coming in.

1. Now, it's time to show how easy it is to make a change to the app and have it updated live on the Kubernetes cluster.

1. Ask the audience to shout out a new option they would like to add to the survey. Pick one and add that to the survey in some code editor (`frontend-service` HTML files). Save the edits and watch as Skaffold automatically picks that up and re-containerizes/re-deploys the app, even quicker this time. Refresh the results page to see the new option show up.

1. Have the audience cast votes with that option available as well and watch as results roll in.

1. (Optional) Shut down Skaffold and show that Skaffold cleans up the services from the cluster. 

## What are the three services?

`frontend-service` - kotlin/ktor

- `/` - take the survey
- `/results` - survey results page

`vote-service` - Spring/Java

- `/` - send vote data to this, publishes to notification-service

`notification-service` - NodeJS

- Subscribes to vote notifications.
