# lightwave
Kotlin interface to Nanoleaf Light panels

This project is comprised of very basic Nanoleaf API client, an Animation framework, and a couple other API clients to tie some functionality together.

In order to use them with a specific set of panels, you'll need to determine address of your device and get an auth token from it.

You can use mDNS to query the device address:
```sh
avahi-browse -r _nanoleafms._tcp
```

You can then set the environment variable $LIGHTWAVE_HOST with the data provided from mDNS.

Likely, the port provided will be incorrect, the default ( and likely ) port is 16021

```sh
export LIGHTWAVE_HOST="{ip}:16021"
```

In order to receive an api key, you'll need to hold the power button on your panels for 5-7 seconds until it starts blinking, then run the `getAuthToken.sh` script.

The panels will issue you an auth token, which you can then set
```sh
export LIGHTWAVE_AUTH="{token}"
```

With auth all set, you can run the demo.

To build the project, run
```sh
./gradlew jar
```

To run it:
```sh
java -jar build/libs/lightwave.jar
```

Take a look at the Server class for an example on how to use the animation DSL.
