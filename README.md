# iconizer-gdx

Generate randomized icons using [libGDX](https://libgdx.com) and [OpenMoji](https://openmoji.org)

## Are the icons any good?

Not in the slightest.

They look like this:

![Sample 1](docs/120019872205304523.png)
![Sample 2](docs/3676769053543825444.png)
![Sample 3](docs/4626224534422258712.png)
![Sample 4](docs/-1823145208856537873.png)
![Sample 5](docs/-7309531037278350805.png)


## Why would I want to use this, then?

These are meant to be used by [gdx-liftoff](https://github.com/libgdx/gdx-liftoff)
to ensure a new project is unlikely to have the same icon as a project previously
submitted to an app marketplace such as the Google Play Store. The Play Store now
suspends apps (including their name and their icon) if they have the same icon or
name as an existing app, so *you should really be putting in more effort before
you submit to the Play Store*, but in case you don't, this acts as a safeguard. 

## OK, you convinced me. How do I use this?

Really? OK, this goes in your core dependencies.

```groovy
implementation 'com.github.tommyettinger:iconizer-gdx:0.1.0'
```

This probably won't work if you're using GL30 compatibility mode; thankfully
GL20 is the default and works in the most places, including here.

This might work on GWT, but I'm not holding my breath. If GWT does work,
then you would use this in your html dependencies:

```groovy
implementation 'com.github.tommyettinger:iconizer-gdx:0.1.0:sources'
```

You will also need this GWT inherits line in your GdxDefinition.gwt.xml file:

```xml
<inherits name="com.github.tommyettinger.iconizer" />
```

## License

The code here is licensed under the [Apache License v2](LICENSE).

The included OpenMoji are licensed under [CC-BY-SA 4.0](OpenMoji-LICENSE.txt).
[The OpenMoji project site is here](https://openmoji.org).