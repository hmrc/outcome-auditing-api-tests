# outcome-auditing-api-tests
API test suite for the `outcome-auditing` using ScalaTest and [play-ws](https://github.com/playframework/play-ws) client.  

## Running the tests

Prior to executing the tests ensure you have:
 - Installed/configured [service manager](https://github.com/hmrc/service-manager).  

Run the following commands to start services locally:

    sm --start OUTCOME_AUDITING --appendArgs '{
        "OUTCOME_AUDITING": [
            "-J-Dauditing.consumer.baseUri.port=6001",
            "-J-Dauditing.consumer.baseUri.host=localhost",
            "-J-Dauditing.enabled=true"
        ]
    }'

## Running specs

Execute the `run_specs.sh` script:

`./run-specs.sh`

## Running ZAP specs - on a developer machine

You can use the `run-local-zap-container.sh` script to build a local ZAP container that will allow you to run ZAP tests locally.  
This will clone a copy of the dast-config-manager repository in this projects parent directory; it will require `make` to be available on your machine.  
https://github.com/hmrc/dast-config-manager/#running-zap-locally has more information about how the zap container is built.

```bash
./run-local-zap-container.sh --start
./run-zap-specs.sh
./run-local-zap-container.sh --stop
``` 

***Note:** Results of your ZAP run will not be placed in your target directory until you have run `./run-local-zap-container.sh --stop`*

***Note:** `./run-local-zap-container.sh` should **NOT** be used when running in a CI environment!*

## Scalafmt

Check all project files are formatted as expected as follows:

```bash
sbt scalafmtCheckAll scalafmtCheck
```

Format `*.sbt` and `project/*.scala` files as follows:

```bash
sbt scalafmtSbt
```

Format all project files as follows:

```bash
sbt scalafmtAll
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
