# Knowledge Grid — Activator

## Description

## Quick Start

1. Download the .jar latest release from https://github.com/grosscol/activator/releases

2. Launch the executable jar (running on port 8080 by default):

    ```bash
    java -jar activator-0.6.4.jar
    ```
3. Test using a built in knowledge object
    ```curl
    curl --request POST \
      --url http://localhost:8080/knowledgeObject/ark:/prescription/counter/result \
      --header 'accept: application/json' \
      --header 'content-type: application/json' \
      --data ' {"DrugIDs":"101 204 708 406 190"}'
    ```

## Further Documentation
See the documentation Readme.md at [docs/Readme.md](docs/Readme.md)
