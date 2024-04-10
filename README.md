# iconizer-gdx

Generate randomized icons using libGDX and OpenMoji

## Are the icons any good?

Not in the slightest.

They look like this:

![Sample 1](docs/2787200039900512181.png)
![Sample 2](docs/4087292332260125602.png)
![Sample 3](docs/7930579473366262530.png)


## Why would I want to use this, then?

These are meant to be used by [gdx-liftoff](https://github.com/libgdx/gdx-liftoff)
to ensure a new project is unlikely to have the same icon as a project previously
submitted to an app marketplace such as the Google Play Store. The Play Store now
suspends apps (including their name and their icon) if they have the same icon or
name as an existing app, so *you should really be putting in more effort before
you submit to the Play Store*, but in case you don't, this acts as a safeguard. 

## OK, you haven't convinced me. How do I use this?

Really? OK, this goes in your core dependencies.

```groovy
implementation 'com.github.tommyettinger:iconizer-gdx:0.0.1'
```

This probably won't work if you're using GL30 compatibility mode; thankfully
GL20 is the default and works in the most places, including here.

This might work on GWT, but I'm not holding my breath. If GWT does work,
then you would use this in your html dependencies:

```groovy
implementation 'com.github.tommyettinger:iconizer-gdx:0.0.1:sources'
```

You will also need this GWT inherits line in your GdxDefinition.gwt.xml file:

```xml
<inherits name="com.github.tommyettinger.iconizer" />
```

## License

The code here is licensed under the [Apache License v2](LICENSE).

The included OpenMoji are licensed under [CC-BY-SA 4.0](OpenMoji-LICENSE.txt).