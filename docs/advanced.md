# Advanced Features

This page is a collection of some less commonly used or more advanced use cases, as well as some of the app's more hidden features.

<a name="share-text"></a>
## Share text into a shortcut

You might want to be able to share a piece of text (e.g. the URL of the current page in a browser) from an app and use it as part of an HTTP request. You can do this with the use of [variables](#variables.md).

When you create or edit a variable, you'll find a checkbox labelled "Allow Receiving Value from Share Dialog". Enable this and save your changes. From now on you can share text from an other app into the HTTP Shortcuts app, and it will use the shared text as the value of that variable, in whatever place you used the variable.

If you use this variable in multiple shortcuts, you will be prompted to choose which shortcut should be executed.

Note that the value that you share will not be stored into that variable, but will only be used temporarily for the execution of the shortcut, meaning that the next time you execute the shortcut that variable will still have its previous value.

See also the [variables documentation](#variables.md#sharing) for more information.

<a name="share-files"></a>
## Share files into a shortcut

If you want to share a file, you can do so by opening the *Request Body / Parameters* section in the shortcut editor and there either set the *Request Body Type* to *File (Picker)* or set it to *Parameters (form-data)* and then add a parameter of type *Single File* or *Multiple Files*. After that save your changes. You should now be able to share files into the HTTP Shortcuts app (the option is called "Send to...") and it will allow you to pick the shortcut as a target. This will execute the shortcut and it will use the content of the shared file as the request body or as a form parameter.

If you have multiple shortcuts that use files in their body, you'll be prompted to select which shortcut should receive the shared file.

<a name="deep-link"></a>
## Trigger shortcut via deep-link

Each shortcut has an associated deep-link URL. You can use this URL to trigger the shortcut from outside the app, by invoking that URL. This is particularly useful if you want to trigger a shortcut by scanning a QR code or an NFC tag. Simply use the shortcut's deep-link URL as the payload for the QR code or NFC tag.

You can get a shortcut's deep-link URL by long-pressing the shortcut in the app's main screen and selecting "Show Info". This will open a dialog window which shows you the URL.

It is also possible to pass additional values to that shortcut, to temporarily override the values of variables used by those shortcuts (similar to how the ["Share into"](#share-text) feature works). Simply append them as query parameters, so e.g. if you have a variable called "myVariable" and you want to invoke a shortcut that uses it, you can do so and pass the value "Hello World" to it via a URL that might look like this:

```
http-shortcuts://f943652a-5f4b-47d9-a4dd-6588292e63dd?myVariable=Hello%20World
```

Make sure to properly URL-encode the value.

## Trigger shortcut via secondary launcher app

In some cases you might not be able to use home screen shortcuts. In this case, as a workaround, the app supports a secondary launcher app, through which shortcuts can be triggered.

To enable this secondary launcher app, open the editor for one of your shortcuts and go to the "Trigger & Execution Settings" screen. There you'll find a "Allow triggering via secondary launcher app" checkbox. Enable this and save your changes. After this you should find the secondary launcher app in your device's list of apps under the name "Trigger shortcut".

If you enable this for multiple shortcuts you'll be prompted to select the shortcut you want to trigger every time you open this secondary app.

Unfortunately, due to technical limitations on Android, it is not possible to change the name or icon of this secondary launcher app.

## Trigger shortcut via Quick Settings Tile

On most Android devices you can pull down the status bar to reveal the quick settings area, e.g. to quickly toggle Wi-Fi or enable "Do not disturb" mode. You can edit this area and choose the tiles that are relevant to you and rearrange them. When you do you'll notice that there's also an HTTP Shortcuts tile called "Trigger shortcut". This tile allows you to quickly trigger a shortcut from anywhere.

To enable a shortcut to be accessible via this quick settings tile, open the editor for it and go to the "Trigger & Execution Settings" screen. There you'll find a "Allow triggering via Quick Settings Tile" checkbox. Enable this and save your changes.

If you enable this for multiple shortcuts you'll be prompted to select the shortcut you want to trigger every time.

## Trigger shortcut via app launcher

Similar to the quick settings tile, another quick way to trigger a shortcut is via the app launcher, i.e., by long-pressing the HTTP Shortcut app's main app icon on the home screen. This will open a menu which shows all the shortcuts which have been enabled to support this.

To enable a shortcut to be accessible via the app launcher, open the editor for it and go to the "Trigger & Execution Settings" screen. There you'll find a "Show as app shortcut on launcher" checkbox. Enable this and save your changes.

Please note that there is a limited number of shortcuts that can be shown on the app launcher. In most cases this limit is set to 5 but the exact number depends on your device's manufacturer.

<a name="integrate-with-tasker"></a>
## Integrating with Tasker

### Trigger a shortcut from Tasker
You can use [Tasker](https://play.google.com/store/apps/details?id=net.dinglisch.android.taskerm) to trigger a shortcut. To pass a value from Tasker to HTTP Shortcuts you need to create a variable of type *Static Variable* in HTTP Shortcuts and a global variable with the same name in Tasker. Make sure to do so BEFORE you select the shortcut from Tasker. All global variables that have matching variables in HTTP Shortcuts are automatically passed over.

You can use the [setResult()](scripting.md#set-result) function (part of the [Scripting feature](scripting.md)) to pass data back to Tasker.

### Trigger a Tasker task from a shortcut

See the [triggerTaskerTask documentation](scripting.md#trigger-tasker-task) for details about triggering a Tasker task.
